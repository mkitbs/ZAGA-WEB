package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.SAPResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.mkgroup.zaga.workorderservice.feign.SAP4HanaProxy;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderMachineRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sun.tools.sjavac.Log;

@Service
public class WorkOrderService {
	
	private static final Logger log = Logger.getLogger(WorkOrderService.class);
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired 
	OperationService operationService;
	
	@Autowired
	CropService cropService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	WorkerHoursService workerHoursService;
	
	@Autowired
	MachineService machineService;
	
	@Autowired
	MaterialService materialService;
	
	@Autowired
	MachineStateService machineStateService;
	
	@Autowired
	SpentMaterialService spentMaterialService;
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	@Autowired
	SpentMaterialRepository spentMaterialRepo;
	
	@Autowired
	WorkOrderMachineRepository womRepo;
	
	@Autowired
	SAP4HanaProxy sap4hana;
	
	public SAPResponse addWorkOrder(WorkOrderDTO workOrderDTO) throws Exception {

			SAPResponse sapResponse = new SAPResponse();
			log.info("Work order creation started");
			
			WorkOrder workOrder = new WorkOrder();
			
			LocalDate startDate = new LocalDate(
					Integer.parseInt(workOrderDTO.getDate().getYear()),
					Integer.parseInt(workOrderDTO.getDate().getMonth()),
					Integer.parseInt(workOrderDTO.getDate().getDay()));
			Date startDateToAdd = startDate.toDate();
			workOrder.setDate(startDateToAdd);
			
			workOrder.setStatus(WorkOrderStatus.NEW);
			workOrder.setCreationDate(new Date());
			
			Operation operation = operationService.getOne(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			
			Crop crop = cropService.getOne(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			
			User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
			
			workOrder.setResponsible(responsible);
			
			workOrder = workOrderRepo.save(workOrder);
			System.out.println(workOrder.getId());//zbog testiranja
			UUID workOrderId = workOrder.getId();
			
			for(WorkOrderWorkerDTO wowDTO : workOrderDTO.getWorkers()) {
				WorkOrderWorker wow = new WorkOrderWorker();
				
				if(wowDTO.getNightPeriod() != null) {
					wow.setNightPeriod(wowDTO.getNightPeriod());
				} else {
					wow.setNightPeriod(-1.0);
				}
				if(wowDTO.getDayPeriod() != null) {
					wow.setDayPeriod(wowDTO.getDayPeriod());
				} else {
					wow.setDayPeriod(-1.0);
				}
				if(wowDTO.getFinalState() != null) {
					wow.setFinalState(wowDTO.getFinalState());
				} else {
					wow.setFinalState(-1.0);
				}
				if(wowDTO.getFuel() != null) {
					wow.setFuel(wowDTO.getFuel());
				} else {
					wow.setFuel(-1.0);
				}
				if(wowDTO.getInitialState() != null) {
					wow.setInitialState(wowDTO.getInitialState());
				} else {
					wow.setInitialState(-1.0);
				}
				if(wowDTO.getInitialState() != null && wowDTO.getFinalState() != null) {
					wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
				} else {
					wow.setSumState(-1.0);
				}
				wow.setWorkOrder(workOrder);
				if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
					wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
				} else {
					wow.setWorkPeriod(-1.0);
				}
				wow.setUser(employeeService.getOne(wowDTO.getUser().getId()));
				wow.setOperation(operationService.getOne(wowDTO.getOperation().getId()));
				wow.setMachine(machineService.getOne(wowDTO.getMachine().getId()));
				
				if(wowDTO.getConnectingMachine().getId() != null) {
					wow.setConnectingMachine(machineService.getOne(wowDTO.getConnectingMachine().getId()));
				}
				wow = wowRepo.save(wow);
				workOrder.getWorkers().add(wow);
				workOrder = workOrderRepo.save(workOrder);
			}
		
			for(SpentMaterialDTO m : workOrderDTO.getMaterials()) {
				SpentMaterial material = new SpentMaterial();

				material.setMaterial(materialService.getOne(m.getMaterial().getId()));
				material.setQuantity(m.getQuantity());
				material.setQuantityPerHectar(m.getQuantity() / workOrder.getCrop().getArea());
				if(m.getSpent() != null) {
					material.setSpent(m.getSpent());
					material.setSpentPerHectar(m.getSpent() / workOrder.getCrop().getArea());
				} else {
					material.setSpent(-1.0);
					material.setSpentPerHectar(-1.0);
				}
				material.setWorkOrder(workOrder);
				material = spentMaterialRepo.save(material);
				workOrder.getMaterials().add(material);
				workOrder = workOrderRepo.save(workOrder);
			}
			WorkOrder wo = getOneW(workOrderId);
			//System.out.println(wo.getWorkers().size()+"AAA");
			Log.info("Work order creation successfuly finished");
			
			WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(wo);

			Map<String, String> headerValues = getHeaderValues();
			String csrfToken = headerValues.get("csrf");
			String authHeader = headerValues.get("authHeader");
			String cookies = headerValues.get("cookies");
		    
		    Log.info("Sending work order to SAP started");
		    ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies,
																"Basic " + authHeader, 
																csrfToken,
																"XMLHttpRequest",
																workOrderSAP);
		    String oDataString = response.toString().replace(":", "-");
		    String formatted = formatJSON(oDataString);
		    
		    Pattern pattern = Pattern.compile("ReturnStatus:(.*?),");
			Matcher matcher = pattern.matcher(formatted);
			String status = "";
			if (matcher.find())
			{
			    status = matcher.group(1);
			}
			
		    
		    if(status.equals("S")) {
		    	System.out.println("USPESNO");
		    	//uspesno
		    	Log.info("Sending work order to SAP successfuly finished");
		    	Pattern patternErpId = Pattern.compile("WorkOrderNumber:(.*?),");
				Matcher matcherId = patternErpId.matcher(formatted);
				Long erpId = 1L;
				if (matcherId.find()){
				    erpId = Long.parseLong(matcherId.group(1));
				}
		    	wo.setErpId(erpId);
		    	workOrderRepo.save(wo);
		    	
		    	sapResponse.setSuccess(true);
		    	sapResponse.setMessage("");
		    	sapResponse.setErpId(erpId);
		    	
		    	log.info("Insert work order into db");
		    }else if(status.equals("E")) {
		    	System.out.println("ERROR");
		    	
		    	String error = "";
		    	//Fail
		    	 Pattern patternMessage = Pattern.compile("MessageText:(.*?),");
					Matcher matcherMessage = patternMessage.matcher(formatted);
					if (matcherMessage.find())
					{
					    error = matcherMessage.group(1);
					}
		    	System.out.println(error);
		    	Log.error("Sending work order to SAP failed. (" + error +")");
		    
		    	workOrderRepo.delete(wo);
		    	
		    	sapResponse.setSuccess(false);
		    	sapResponse.setMessage(error);
		    	
		    	log.info("Insert work order into db failed");
		
		    }
		    return sapResponse;
		}
	
	public List<WorkOrderDTO> getAll(){
		List<WorkOrder> workOrders = workOrderRepo.findAllOrderByCreationDate();
		List<WorkOrderDTO> workOrdersDTO = new ArrayList<WorkOrderDTO>();
		for(WorkOrder workOrder : workOrders) {
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			workOrdersDTO.add(workOrderDTO);
		}
		return workOrdersDTO;
	}
	
	public WorkOrderDTO getOne(UUID id) {
		try {
			WorkOrder workOrder = workOrderRepo.getOne(id);
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			return workOrderDTO;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public WorkOrder getOneW(UUID id) {
		try {
			WorkOrder workOrder = workOrderRepo.getOne(id);
			return workOrder;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void updateWorkOrder(WorkOrderDTO workOrderDTO) {
		try {
			log.info("Work order update started");
			
			WorkOrder workOrder = workOrderRepo.getOne(workOrderDTO.getId());
			
			LocalDate startDate = new LocalDate(
					Integer.parseInt(workOrderDTO.getDate().getYear()),
					Integer.parseInt(workOrderDTO.getDate().getMonth()),
					Integer.parseInt(workOrderDTO.getDate().getDay()));
			Date startDateToAdd = startDate.toDate();
			workOrder.setDate(startDateToAdd);
			
			if(workOrderDTO.getStatus().equals("Novi")) {
				workOrder.setStatus(WorkOrderStatus.NEW);
			} else if(workOrderDTO.getStatus().equals("U radu")) {
				workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
			} else {
				workOrder.setStatus(WorkOrderStatus.CLOSED);
			}
			
			workOrder.setCreationDate(workOrderDTO.getCreationDate());
			
			Operation operation = operationService.getOne(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			
			Crop crop = cropService.getOne(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			
			User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
			
			workOrder.setResponsible(responsible);
			
			workOrder = workOrderRepo.save(workOrder);
	
		}catch(Exception e) {
			log.error("Update work order faild", e);
		}
	}
	
	public WorkOrder createCopy(WorkOrder workOrder, Date date) {
		WorkOrder copy = new WorkOrder();
		List<WorkOrderWorker> workers = workOrder.getWorkers();
		List<SpentMaterial> materials = workOrder.getMaterials();
		copy.setCreationDate(new Date());
		copy.setResponsible(workOrder.getResponsible());
		copy.setCrop(workOrder.getCrop());
		copy.setOperation(workOrder.getOperation());
		copy.setTreated(0);
		copy.setClosed(false);
		copy.setWorkers(new ArrayList<WorkOrderWorker>());
		copy.setMaterials(new ArrayList<SpentMaterial>());
		copy.setDate(date);
		copy.setStatus(WorkOrderStatus.IN_PROGRESS);
		copy = workOrderRepo.save(copy);
		System.out.println(copy.getId());
		
		for(WorkOrderWorker worker : workers) {
			WorkOrderWorker wow = new WorkOrderWorker();
			wow.setWorkOrder(copy);
			wow.setMachine(worker.getMachine());
			wow.setUser(worker.getUser());
			wow.setOperation(worker.getOperation());
			wow.setConnectingMachine(worker.getConnectingMachine());
			
			wowRepo.save(wow);
		}
		
		for(SpentMaterial material:materials) {
			SpentMaterial spentMaterial = new SpentMaterial();
			spentMaterial.setMaterial(material.getMaterial());
			spentMaterial.setWorkOrder(copy);
			
			spentMaterialRepo.save(spentMaterial);
		}
		return copy;
	}
	

	public void closeWorkOrder(WorkOrderDTO workOrderDTO) {
		try {
			WorkOrder workOrder = workOrderRepo.getOne(workOrderDTO.getId());
			//this.updateWorkOrder(workOrderDTO);
			workOrder.setTreated(workOrderDTO.getTreated());
			workOrder.setStatus(WorkOrderStatus.CLOSED);
			workOrder.setClosed(true);
			workOrderRepo.save(workOrder);
		} catch(Exception e) {
			log.error("Update work order faild", e);
		}
	}
	
	public List<WorkOrderDTO> getAllByStatus(WorkOrderStatus status){
		List<WorkOrder> workOrders = workOrderRepo.findAllByStatus(status);
		List<WorkOrderDTO> workOrdersDTO = new ArrayList<WorkOrderDTO>();
		for(WorkOrder workOrder : workOrders) {
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			workOrdersDTO.add(workOrderDTO);
		}
		return workOrdersDTO;
	}

	public String formatJSON(String json) {
		json = json.replace("=", ":");
		json = json.replaceAll("__metadata:\\{[a-zA-Z0-9,':=\".()/_ -]*\\},", "");
		json = json.replace("/", "");
		json = json.replaceAll(":,", ":\"\",");
		json = json.replaceAll(":}", ":\"\"}");
		json = json.replaceAll("<201 [a-zA-Z ]+,", "");
		json = json.replaceAll(",\\[content[-a-zA-Z0-9,\". ;:_()'\\]<>]+", "");
		//System.out.println(json);
		
		return json;

	}
	
	public Map<String, String> getHeaderValues() {
		Log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder()
				.append("MKATIC")
				.append(":")
				.append("katicm0908");
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		
		ResponseEntity<Object> resp = sap4hana.getCSRFToken("Basic " + authHeader, "Fetch");
		
		Log.info("Getting X-CSRF-Token successfuly finished");
		
		HttpHeaders headers = resp.getHeaders();
		String csrfToken;
		csrfToken = headers.getValuesAsList("x-csrf-token").stream()
		                                                   .findFirst()
		                                                   .orElse("nema");
		
		String cookies = headers.getValuesAsList("Set-Cookie")
				.stream()
				.collect(Collectors.joining(";"));
		
		Map<String, String> results = new HashMap<String, String>();
		results.put("csrf", csrfToken);
		results.put("authHeader", authHeader);
		results.put("cookies", cookies);
		return results;
	}
	
}