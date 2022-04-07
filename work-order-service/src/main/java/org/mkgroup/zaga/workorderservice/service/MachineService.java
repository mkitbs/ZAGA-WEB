package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineReportDTO;
import org.mkgroup.zaga.workorderservice.dto.MaterialReportDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.UserAuthDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderForMachineReportDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderForMaterialReportDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerReportDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.feign.SearchServiceProxy;
import org.mkgroup.zaga.workorderservice.model.FuelType;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.MachineGroup;
import org.mkgroup.zaga.workorderservice.model.MachineType;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.MachineGroupRepository;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MachineService {

	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	SearchServiceProxy ssProxy;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	MachineRepository machineRepository;
	
	@Autowired
	MachineGroupRepository machineGroupRepo;
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	@Autowired
	WorkOrderService workOrderService;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	public List<MachineDTO> getMachinesFromSAP() throws JSONException {
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		//Call SAP and retrieve cultureGroupSet
		ResponseEntity<Object> machineSet = 
				gwProxy.fetchMachines("json", "Basic " + authHeader);
		String oDataString = machineSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		
		System.out.println("JSON2 => " + oDataString);
		
		//Map to specific object
	    ArrayList<MachineDTO> machineList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));

	    for(MachineDTO m : machineList) {
	    	System.out.println(m.getName());
	    	machineRepository
	    		.findByErpIdAndOrgUnit(m.getErpId(), m.getOrgUnit())
	    		.ifPresentOrElse(foundMachine -> updateMachine(foundMachine, m), 
	    						() -> createMachine(m));
	    }
	    
		return machineList;
	}
	
	public ArrayList<MachineDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<MachineDTO> convertedList= new ArrayList<MachineDTO>();
	    list.forEach(objectOfAList -> {
	    	MachineDTO machineDto = new MachineDTO();
	    	
			try {
				machineDto = objectMapper
											.readValue(objectOfAList.toString(),
													MachineDTO.class);
				convertedList.add(machineDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    });
	    return convertedList;
	}

	public String formatJSON(String json) {
		json = json.replace("=", ":");
		System.out.println("JSON => " + json);
		json = json.replaceAll("__metadata:\\{[%a-zA-Z0-9,':=\".()/_\\n -]*\\},", "");
		json = json.replace("/", "");
		json = json.replaceAll(":,", ":\"\",");
		json = json.replaceAll(":}", ":\"\"}");
		return json;
	}
	
	public Machine getOne(UUID id) {
		try {
			Machine machine = machineRepository.getOne(id);
			return machine;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void updateMachine(Machine oldMachine, MachineDTO newMachine) {
		oldMachine.setCompanyCode(newMachine.getCompanyCode());
		oldMachine.setFuelType(FuelType.values()[Integer.parseInt(newMachine.getFuelType())]);
		String newName;
		if(newMachine.getName() != null) {
			newName = newMachine.getName().replaceAll("%22", "'");//kada " stigne sa erp-a salje se kao %22
			oldMachine.setName(newName);
		}else {
			oldMachine.setName(newMachine.getName());
		}
		if(newMachine.getType().equals("PG")) {
			oldMachine.setType(MachineType.PROPULSION);
		} else if(newMachine.getType().equals("PR")) {
			oldMachine.setType(MachineType.COUPLING);
		} else {
			oldMachine.setType(MachineType.PROPULSION);
		}
		
		if(newMachine.getFuelErpId() != null) {
			oldMachine.setFuelErpId(newMachine.getFuelErpId());
		}else {
			oldMachine.setFuelErpId(0L);
		}
		
		oldMachine.setOrgUnit(newMachine.getOrgUnit());
		MachineGroup machineGroup = machineGroupRepo.findByErpId(newMachine.getMachineGroupId()).get();
		oldMachine.setMachineGroupId(machineGroup);
		machineRepository.save(oldMachine);
	}
	
	public void createMachine(MachineDTO newMachine) {
		Machine m = machineRepository.findByErpId(newMachine.getErpId()).orElse(null);
		if(m == null) {
			if(newMachine.getType().equals("PG") || newMachine.getType().equals("PR") || newMachine.getType().equals("N")) {
				Machine machine = new Machine(newMachine);
				if(machine.getName() != null) {
					machine.setName(machine.getName().replaceAll("%22", "'"));
				}
				MachineGroup machineGroup = machineGroupRepo.findByErpId(newMachine.getMachineGroupId()).get();
				machine.setMachineGroupId(machineGroup);
				machineRepository.save(machine);
			}
		}
		
	}
	
	public void editMachine(MachineDTO machineDTO) {
		Machine machine = machineRepository.getOne(UUID.fromString(machineDTO.getDbid()));
		if(machineDTO.getType().equals("PRIKLJUČNA")) {
			machine.setType(MachineType.COUPLING);
		} else if (machineDTO.getType().equals("POGONSKA")) {
			machine.setType(MachineType.PROPULSION);
		}
		switch(machineDTO.getFuelType()) {
		case "NIJE IZABRANO":
			machine.setFuelType(FuelType.NOT_SELECTED);
			break;
		case "BENZIN":
			machine.setFuelType(FuelType.GASOLINE);
			break;
		case "GAS":
			machine.setFuelType(FuelType.GAS);
			break;
		case "EVRO DIZEL":
			machine.setFuelType(FuelType.EURO_DIESEL);
			break;
		case "BIO DIZEL":
			machine.setFuelType(FuelType.BIO_DIESEL);
			break;
		case "DIZEL":
			machine.setFuelType(FuelType.DIESEL);
			break;
		default:
			break;
		}
		MachineGroup machineGroup = machineGroupRepo.getOne(machineDTO.getMachineGroup());
		machine.setMachineGroupId(machineGroup);
		machineRepository.save(machine);
	}
	
	public List<MachineDTO> getAllByGroup(UUID id) {
		List<Machine> machines = machineRepository.getAllByMachineGroup(id);
		List<MachineDTO> retVals = new ArrayList<MachineDTO>();
		for(Machine machine : machines) {
			MachineDTO retVal = new MachineDTO(machine);
			retVals.add(retVal);
		}
		
		return retVals;
	}
	
	
	public List<MachineReportDTO> getMachinesForReport(Long tenantId){
		List<WorkOrderWorker> wows = wowRepo.findAllByOrderByMachineId(tenantId);
		//List<WorkOrderForMachineReportDTO> workOrders = workOrderRepo.findAllByMachine(tenantId);
		List<MachineReportDTO> retValues = new ArrayList<MachineReportDTO>();
		
		int counter=0;
		
			for(WorkOrderWorker wow : wows) {
				MachineReportDTO report = new MachineReportDTO();
				//List<WorkOrderForMachineReportDTO> workOrders = new ArrayList<WorkOrderForMachineReportDTO>();
				//List<WorkOrderWorker> workOrdersForMaterial = workOrders.stream().filter(wo -> wo.getMachine().getId().toString().equals(wow.getMachine().getId().toString())).collect(Collectors.toList());
				//List<WorkOrderForMachineReportDTO> workOrders = workOrderRepo.findAllByMachine(tenantId);
				//List<WorkOrder> workOrders = workOrderRepo.findAllByMachineId(tenantId, wow.getMachine().getId());
				System.out.println(counter);
				List<String> workOrders = wowRepo.findAllByMachine(wow.getMachine().getId());
				
				report.setMachine(wow.getMachine().getName());
				report.setWorkOrders(workOrders);
				retValues.add(report);
				counter++;
			}
		
		
		System.out.println("sum = " + retValues.size());
		return retValues;
	}
	
	public List<MachineDTO> getAllGrouByMachineType(){
		List<Machine> machines = machineRepository.findAllByGroupByType();
		List<MachineDTO> retValues = new ArrayList<MachineDTO>();
		for(Machine machine : machines) {
			MachineDTO machineDTO = new MachineDTO(machine);
			retValues.add(machineDTO);
		}
		return retValues;
	}
}
