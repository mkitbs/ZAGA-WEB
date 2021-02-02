package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineReportDTO;
import org.mkgroup.zaga.workorderservice.dto.UserAuthDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerReportDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.feign.SearchServiceProxy;
import org.mkgroup.zaga.workorderservice.model.FuelType;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.MachineGroup;
import org.mkgroup.zaga.workorderservice.model.MachineType;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.MachineGroupRepository;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
		
		//Map to specific object
	    ArrayList<MachineDTO> machineList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));

	    for(MachineDTO m : machineList) {
	    	machineRepository
	    		.findByErpId(m.getErpId())
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
		json = json.replaceAll("__metadata:\\{[a-zA-Z0-9,%':=\".()/_ -]*\\},", "");
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
		oldMachine.setName(newMachine.getName());
		oldMachine.setOrgUnit(newMachine.getOrgUnit());
		MachineGroup machineGroup = machineGroupRepo.findByErpId(newMachine.getMachineGroupId()).get();
		oldMachine.setMachineGroupId(machineGroup);
		machineRepository.save(oldMachine);
	}
	
	public void createMachine(MachineDTO newMachine) {
		if(newMachine.getType().equals("PG") || newMachine.getType().equals("PR")) {
			Machine machine = new Machine(newMachine);
			MachineGroup machineGroup = machineGroupRepo.findByErpId(newMachine.getMachineGroupId()).get();
			machine.setMachineGroupId(machineGroup);
			machineRepository.save(machine);
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
	
	public List<MachineReportDTO> getMachinesForReport(String sapUserId){
		RestTemplate rest = new RestTemplate();
		HttpServletRequest requesthttp = 
		        ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
		                .getRequest();

		String token = (requesthttp.getHeader("Token"));
		System.out.println(token);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<String> request2send = new HttpEntity<String>(headers);
		ResponseEntity<UserAuthDTO> user = rest.exchange(
				"http://localhost:8091/user/getUserBySapId/"+sapUserId, 
				HttpMethod.GET, request2send, new ParameterizedTypeReference<UserAuthDTO>(){});
		List<WorkOrderWorker> wows = wowRepo.findAllByOrderByMachineId(user.getBody().getTenant().getId());
		MachineReportDTO report = new MachineReportDTO();
		List<MachineReportDTO> retValues = new ArrayList<MachineReportDTO>();
		if(wows.size() > 0) {
			report.setMachine(new MachineDTO(wows.get(0).getMachine()));
			report.getWorkOrders().add(new WorkOrderDTO(wows.get(0).getWorkOrder(), wows.get(0).getMachine().getName()));
			if(wows.size() == 1) {
				retValues.add(report);
			}
		}
		for(int i = 0; i<wows.size()-1; i++) {
			if(wows.get(i).getMachine().getId().equals(wows.get(i+1).getMachine().getId())) {
				if(!wows.get(i).getWorkOrder().equals(wows.get(i+1).getWorkOrder())) {
					report.getWorkOrders().add(new WorkOrderDTO(wows.get(i+1).getWorkOrder(), wows.get(i+1).getMachine().getName()));
				}
				
				if(i+1 == wows.size()-1) {
					retValues.add(report);
				}
			} else {
				retValues.add(report);
				report = new MachineReportDTO();
				report.setMachine(new MachineDTO(wows.get(i+1).getMachine()));
				report.getWorkOrders().add(new WorkOrderDTO(wows.get(i+1).getWorkOrder(), wows.get(i+1).getMachine().getName()));
				if(i+1 == wows.size()-1) {
					retValues.add(report);
				}
			}
			
		}
		
		for(MachineReportDTO r : retValues) {
			double finalStateSum = 0.0;
			double fuelSum = 0.0;
			for(WorkOrderDTO wo : r.getWorkOrders()) {
				for(WorkOrderWorkerDTO wow : wo.getWorkers()) {
					if(wow.getSumState() == -1) {
						wow.setSumState(0.0);
					}
					finalStateSum += wow.getSumState();
					if(wow.getFuel() == -1) {
						wow.setFuel(0.0);
					}
					fuelSum += wow.getFuel();
				}
			}
			r.setFinalStateSum(finalStateSum);
			r.setFuelSum(fuelSum);
		}
		
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
