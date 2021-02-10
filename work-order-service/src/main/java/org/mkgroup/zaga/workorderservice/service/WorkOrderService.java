package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.mkgroup.zaga.workorderservice.dto.DateDTO;
import org.mkgroup.zaga.workorderservice.dto.OperationsTodayDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.UserAuthDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.CloseWorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.CloseWorkOrderResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.SAPResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderEmployeeSAP;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.mkgroup.zaga.workorderservice.feign.SAP4HanaProxy;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorkerStatus;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderMachineRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
	MaterialRepository materialRepo;
	
	@Autowired
	SAP4HanaProxy sap4hana;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${sap.services.s4h}")
	String sapS4Hurl;
	
	@Value("${sap.services.s4h_close}")
	String sapS4Close;
	
	public SAPResponse addWorkOrder(WorkOrderDTO workOrderDTO, String sapUserId, Long tenantId) throws Exception {

			SAPResponse sapResponse = new SAPResponse();
			log.info("Work order creation started");
			
			WorkOrder workOrder = new WorkOrder();
			
			LocalDate startDate = new LocalDate(
					Integer.parseInt(workOrderDTO.getDate().getYear()),
					Integer.parseInt(workOrderDTO.getDate().getMonth()),
					Integer.parseInt(workOrderDTO.getDate().getDay()));
			Date startDateToAdd = startDate.toDate();
			workOrder.setDate(startDateToAdd);
			
			workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
			workOrder.setCreationDate(new Date());
			workOrder.setTreated(0);
			
			Operation operation = operationService.getOne(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			
			Crop crop = cropService.getOne(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			
			User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
			
			workOrder.setResponsible(responsible);
			workOrder.setUserCreatedSapId(Long.parseLong(sapUserId));
			workOrder.setTenantId(tenantId);
			workOrder.setNumOfRefOrder(workOrderDTO.getNumOfRefOrder());
			workOrder.setNote(workOrderDTO.getNote());
			workOrder = workOrderRepo.save(workOrder);
			
			UUID workOrderId = workOrder.getId();
			
			Map<Long, Boolean> fuelsMap = new HashMap<Long, Boolean>();
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
				wow.setUser(employeeService.getOne(wowDTO.getUser().getUserId()));
				wow.setOperation(operationService.getOne(wowDTO.getOperation().getId()));
				Machine machine = machineService.getOne(UUID.fromString(wowDTO.getMachine().getDbid()));
				wow.setMachine(machine);
				wow.setStatus(WorkOrderWorkerStatus.NOT_STARTED);
				
				if(!fuelsMap.containsKey(machine.getFuelErpId())) {
					fuelsMap.put(machine.getFuelErpId(), true);
				}
				
				if(!wowDTO.getConnectingMachine().getDbid().equals("-1")) {
					wow.setConnectingMachine(machineService.getOne(UUID.fromString(wowDTO.getConnectingMachine().getDbid())));
				}else {
					wow.setConnectingMachine(null);
				}
				wow = wowRepo.save(wow);
				workOrder.getWorkers().add(wow);
				workOrder = workOrderRepo.save(workOrder);
			}
		
			for(SpentMaterialDTO m : workOrderDTO.getMaterials()) {
				SpentMaterial material = new SpentMaterial();

				material.setMaterial(materialService.getOne(m.getMaterial().getDbid()));
				material.setQuantity(m.getQuantity());
				//material.setQuantityPerHectar(m.getQuantity() / workOrder.getCrop().getArea());
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
			
			Iterator it = fuelsMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        System.out.println((Long) pair.getKey());
		        SpentMaterial sm = new SpentMaterial();
		        if((Long) pair.getKey() != 0) {
		        	sm.setMaterial(materialRepo.findByErpId((Long) pair.getKey()).get());
			        sm.setQuantity(-1.0);
					sm.setSpent(-1.0);
					sm.setSpentPerHectar(-1.0);
					sm.setFuel(true);
					sm.setWorkOrder(workOrder);
					sm = spentMaterialRepo.save(sm);
					workOrder.getMaterials().add(sm);
					workOrder = workOrderRepo.save(workOrder);
		        }
		        
		        it.remove(); // avoids a ConcurrentModificationException
		    }
			
			WorkOrder wo = getOneW(workOrderId);
			
			log.info("Work order creation successfuly finished");
			
			WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(wo, "NEW");
			Map<String, String> headerValues = getHeaderValues(wo);
			String csrfToken = headerValues.get("csrf");
			String authHeader = headerValues.get("authHeader");
			String cookies = headerValues.get("cookies");
		    
		    log.info("Sending work order to SAP started");
		    
		    /*
		    ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies,
																"Basic " + authHeader, 
																csrfToken,
																"XMLHttpRequest",
																workOrderSAP);
			*/
		    HttpHeaders headersRestTemplate = new HttpHeaders();
	  		headersRestTemplate.set("Authorization", "Basic " + authHeader);
	  		headersRestTemplate.set("X-CSRF-Token", csrfToken);
	  		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
	  		headersRestTemplate.set("Cookie", cookies);
	  		System.out.println("Token:" + csrfToken);
	  		System.out.println(headersRestTemplate.toString());
	  		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

	  		ResponseEntity<Object> response = null;
	  		try {
	  			response = restTemplate.exchange(
	  		  		    sapS4Hurl, HttpMethod.POST, entity, Object.class);
	  		} catch(Exception e) {
	  			workOrderRepo.delete(wo);
	  		}
	  		
		  	
	  		//System.out.println("Rest Template Testing SAP WO: " + response.getBody().toString());
		    
		    if(response == null) {
		    	workOrderRepo.delete(wo);
				throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		    }
		    
		    String oDataString = response.getBody().toString().replace(":", "-");
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
		    	System.out.println("FORMATTED => " + formatted);
		    	JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
		    	JsonArray array = convertedObject.get("d").getAsJsonObject().get("WorkOrderToEmployeeNavigation").getAsJsonObject().get("results").getAsJsonArray();
			    JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();
			    System.out.println("WorkOrderToMaterialNavigation =>" + arrayMaterial);
			    System.out.println("WorkOrderToEmployeeNavigation => " + array);
		    	
		    	for(int i = 0; i <array.size(); i++) {
		    		UUID uid = UUID.fromString(array.get(i).getAsJsonObject().get("WebBackendId").getAsString());
		    		WorkOrderWorker wow = wowRepo.getOne(uid);
		    		System.out.println("NASAO" + wow.getMachine().getName());
		    		wow.setErpId(array.get(i).getAsJsonObject().get("WorkOrderEmployeeNumber").getAsInt());
		    		wowRepo.save(wow);
		    	}
		    	
		    	for(int i = 0; i <arrayMaterial.size(); i++) {
		    		if(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString().equals("")) {
		    			SpentMaterial sm = new SpentMaterial();
		    			//long id = 10000049;
		    			//Material material = materialRepo.findByErpId(id).get();
		    			//Material material = materialRepo.findByErpId(arrayMaterial.get(i).getAsJsonObject().get("MaterialId").getAsLong()).get();
		    			//sm.setMaterial(material);
		    			sm.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
		    			spentMaterialRepo.save(sm);
		    		} else {
		    			UUID uid = UUID.fromString(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString());
			    		SpentMaterial spentMat = spentMaterialRepo.getOne(uid);
			    		spentMat.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
			    		spentMaterialRepo.save(spentMat);
		    		}
		    		
		    		
		    		
		    	}
		    	
		    	log.info("Sending work order to SAP successfuly finished");
		    	Pattern patternErpId = Pattern.compile("WorkOrderNumber:(.*?),");
				Matcher matcherId = patternErpId.matcher(formatted);
				Long erpId = 1L;
				if (matcherId.find()){
				    erpId = Long.parseLong(matcherId.group(1));
				}
		    	wo.setErpId(erpId);
		    	workOrderRepo.save(wo);
		    	
		    	sapResponse.setSuccess(true);
		    	sapResponse.setErpId(erpId);
		    	
		    	log.info("Insert work order into db");
		    }else if(status.equals("E")) {
		    	System.out.println("ERROR");
		    	
		    	
		    	//Fail
		    	 Pattern patternError = Pattern.compile("MessageText:(.*?),");
		    	 Matcher matcherError = patternError.matcher(formatted);
				List<String> errors = new ArrayList<String>();
				matcherError.results().forEach(mat -> errors.add((mat.group(1))));
		   
		    	workOrderRepo.delete(wo);
		    	
		    	sapResponse.setSuccess(false);
		    	sapResponse.setMessage(errors);
		    	
		    	log.info("Insert work order into db failed");
		
		    }
		    return sapResponse;
		}
	
	public List<WorkOrderDTO> getAll(Long tenantId){
		List<WorkOrder> workOrders = workOrderRepo.findAllOrderByCreationDate();
		List<WorkOrderDTO> workOrdersDTO = new ArrayList<WorkOrderDTO>();
		
		for(WorkOrder workOrder : workOrders) {
			if(tenantId == workOrder.getTenantId()) {
				WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
				workOrdersDTO.add(workOrderDTO);
			}
		}
		return workOrdersDTO;
	}
	
	public List<WorkOrderDTO> getMyWorkOrders(Long tenantId, String sapUserId){
		List<WorkOrder> workOrders = workOrderRepo.findMyOrderByCreationDate(Long.parseLong(sapUserId), tenantId);
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
	
	public SAPResponse updateDataWorkOrder(WorkOrder workOrder) throws Exception {
		SAPResponse retVal = new SAPResponse();
		
		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");
		
		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");
		
		log.info("Updating work order with employee to SAP started");
	    
		HttpHeaders headersRestTemplate = new HttpHeaders();
  		headersRestTemplate.set("Authorization", "Basic " + authHeader);
  		headersRestTemplate.set("X-CSRF-Token", csrfToken);
  		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
  		headersRestTemplate.set("Cookie", cookies);
  		System.out.println("Token:" + csrfToken);
  		System.out.println(headersRestTemplate.toString());
  		System.out.println("KA SAPU -> " + workOrderSAP.toString());
  		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);
		
  		ResponseEntity<Object> response = restTemplate.exchange(
	  		    sapS4Hurl, HttpMethod.POST, entity, Object.class);
  		
		System.out.println(response);
	    if(response == null) {
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    System.out.println("REZZ" + response.getBody());
	    
	    String oDataString = response.toString().replace(":", "-");
	    String formatted = formatJSON(oDataString);
	    System.out.println(formatted + "ASASA");
	    Pattern pattern = Pattern.compile("ReturnStatus:(.*?),");
		Matcher matcher = pattern.matcher(formatted);
		String status = "";
		if (matcher.find())
		{
		    status = matcher.group(1);
		}
		
	    
	    if(status.equals("S")) {
	    	System.out.println("USPESNO DODAT");
	    	retVal.setSuccess(true);
	    	retVal.getMessage().add("Radni nalog uspesno azuriran");
	    }else if(status.equals("E")){
	    	System.out.println("ERROR");
	    	retVal.setSuccess(false);
	    	retVal.getMessage().add("Greska prilikom azuriranja");
	    }
	
	    return retVal;
	} 
	
	public SAPResponse updateWorkOrder(WorkOrderDTO workOrderDTO) throws Exception {

		log.info("Work order update started");
		
		SAPResponse sapResponse = new SAPResponse();
		
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
		
		workOrder.setTreated(workOrderDTO.getTreated());
		
		workOrder.setCreationDate(workOrderDTO.getCreationDate());
		
		Operation operation = operationService.getOne(workOrderDTO.getOperationId());
		workOrder.setOperation(operation);
		
		Crop crop = cropService.getOne(workOrderDTO.getCropId());
		workOrder.setCrop(crop);
		
		User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
		
		workOrder.setResponsible(responsible);
		workOrder.setNumOfRefOrder(workOrderDTO.getNumOfRefOrder());
		workOrder.setNote(workOrderDTO.getNote());
		

		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");
		
		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");
		for(WorkOrderEmployeeSAP emp : workOrderSAP.getWorkOrderToEmployeeNavigation().getResults()) {
			if(workOrderDTO.isNoOperationOutput()) {
				System.out.println("NO OPERATION OUTPUT");
				emp.setNoOperationOutput("X");
				workOrder.setNoOperationOutput(true);
			} else {
				workOrder.setNoOperationOutput(false);
			}
		}
		
		log.info("Updating work order with employee to SAP started");
	    /*ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies,
															"Basic " + authHeader, 
															csrfToken,
															"XMLHttpRequest",
															workOrderSAP);
		*/
		HttpHeaders headersRestTemplate = new HttpHeaders();
  		headersRestTemplate.set("Authorization", "Basic " + authHeader);
  		headersRestTemplate.set("X-CSRF-Token", csrfToken);
  		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
  		headersRestTemplate.set("Cookie", cookies);
  		System.out.println("Token:" + csrfToken);
  		System.out.println(headersRestTemplate.toString());
  		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);
  		
  		System.out.println("TO SAP => " + workOrderSAP);
		
  		ResponseEntity<Object> response = restTemplate.exchange(
	  		    sapS4Hurl, HttpMethod.POST, entity, Object.class);
  		
		System.out.println(response);
	    if(response == null) {
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    System.out.println("REZZ" + response.getBody());
	    
	    String oDataString = response.toString().replace(":", "-");
	    String formatted = formatJSON(oDataString);
	    System.out.println(formatted + "ASASA");
	    Pattern pattern = Pattern.compile("ReturnStatus:(.*?),");
		Matcher matcher = pattern.matcher(formatted);
		String status = "";
		if (matcher.find())
		{
		    status = matcher.group(1);
		}
		
	    
	    if(status.equals("S")) {
	    	System.out.println("USPESNO DODAT");
	    	workOrder = workOrderRepo.save(workOrder);
	    	sapResponse.setSuccess(true);
	    	
	    	
	    }else if(status.equals("E")){
	    	System.out.println("ERROR");
	    	String error = "";
	    	//Fail
	    	Pattern patternError = Pattern.compile("MessageText:(.*?),");
	    	 Matcher matcherError = patternError.matcher(formatted);
			List<String> errors = new ArrayList<String>();
			matcherError.results().forEach(mat -> errors.add((mat.group(1))));
	    	System.out.println(error);
	    	log.error("Updating work order to SAP failed. (" + error +")");
	    	
	    	sapResponse.setSuccess(false);
	    	sapResponse.setMessage(errors);
	    	
	    	
	    }
	    return sapResponse;
	}
	
	public WorkOrder createCopy(WorkOrder workOrder, DateDTO copyDate, String sapUserId, Long tenantId) throws Exception {
		
		SAPResponse sapResponse = new SAPResponse();
		
		LocalDate newDate = new LocalDate(Integer.parseInt(copyDate.getYear()),
				  Integer.parseInt(copyDate.getMonth()),
				  Integer.parseInt(copyDate.getDay()));
		Date date = newDate.toDate();
		
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
		copy.setUserCreatedSapId(Long.parseLong(sapUserId));
		copy.setTenantId(tenantId);
		
		copy = workOrderRepo.save(copy);
		UUID workOrderId = copy.getId();
		
		for(WorkOrderWorker worker : workers) {
			
			WorkOrderWorker wow = new WorkOrderWorker();
			wow.setWorkOrder(copy);
			wow.setMachine(worker.getMachine());
			wow.setUser(worker.getUser());
			wow.setOperation(worker.getOperation());
			wow.setConnectingMachine(worker.getConnectingMachine());
			wow.setStatus(WorkOrderWorkerStatus.NOT_STARTED);
			wowRepo.save(wow);
			copy.getWorkers().add(wow);
			copy = workOrderRepo.save(copy);
		}
		
		for(SpentMaterial material:materials) {
			SpentMaterial spentMaterial = new SpentMaterial();
			spentMaterial.setMaterial(material.getMaterial());
			if(material.isFuel()) {
				spentMaterial.setFuel(true);
			}
			spentMaterial.setWorkOrder(copy);
			
			spentMaterialRepo.save(spentMaterial);
			copy.getMaterials().add(spentMaterial);
			copy = workOrderRepo.save(copy);
		}
		
		WorkOrder wo = getOneW(workOrderId);
		log.info("Work order creation successfuly finished");
		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(wo, "NEW");

		Map<String, String> headerValues = getHeaderValues(wo);
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");
	    
	    log.info("Sending work order to SAP started");
	    /*
	    ResponseEntity<?> response = sap4hana.sendWorkOrder(cookies,
															"Basic " + authHeader, 
															csrfToken,
															"XMLHttpRequest",
															workOrderSAP);
		*/
	    //Testing HTTPS with RestTemplate
  		HttpHeaders headersRestTemplate = new HttpHeaders();
  		headersRestTemplate.set("Authorization", "Basic " + authHeader);
  		headersRestTemplate.set("X-CSRF-Token", csrfToken);
  		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
  		headersRestTemplate.set("Cookie", cookies);
  		
  		
  		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

  		ResponseEntity<Object> response = restTemplate.postForEntity(
  		    sapS4Hurl, entity, Object.class);
	  	
  		//System.out.println("Rest Template Testing SAP WO: " + response.getBody().toString());
	    
  		if(response == null) {
	    	workOrderRepo.delete(wo);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    String oDataString = response.getBody().toString().replace(":", "-");
	    String formatted = formatJSON(oDataString);
	    System.out.println(formatted);
	    JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
	    JsonArray array = convertedObject.get("d").getAsJsonObject().get("WorkOrderToEmployeeNavigation").getAsJsonObject().get("results").getAsJsonArray();
	    JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();
	    System.out.println("REZ2" + array.toString());
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
	    	for(int i = 0; i <array.size(); i++) {
	    		UUID uid = UUID.fromString(array.get(i).getAsJsonObject().get("WebBackendId").getAsString());
	    		WorkOrderWorker wow = wowRepo.getOne(uid);
	    		System.out.println("NASAO" + wow.getMachine().getName());
	    		wow.setErpId(array.get(i).getAsJsonObject().get("WorkOrderEmployeeNumber").getAsInt());
	    		wowRepo.save(wow);
	    	}
	    	
	    	for(int i = 0; i <arrayMaterial.size(); i++) {
	    		UUID uid = UUID.fromString(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString());
	    		SpentMaterial spentMat = spentMaterialRepo.getOne(uid);
	    		
	    		spentMat.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
	    		spentMaterialRepo.save(spentMat);
	    	}
	    	
	    	log.info("Sending work order to SAP successfuly finished");
	    	Pattern patternErpId = Pattern.compile("WorkOrderNumber:(.*?),");
			Matcher matcherId = patternErpId.matcher(formatted);
			Long erpId = 1L;
			if (matcherId.find()){
			    erpId = Long.parseLong(matcherId.group(1));
			}
	    	wo.setErpId(erpId);
	    	workOrderRepo.save(wo);
	    	
	    	sapResponse.setSuccess(true);
	    	sapResponse.setErpId(erpId);
	    	
	    	log.info("Insert work order into db");
	    }else if(status.equals("E")) {
	    	System.out.println("ERROR");
	    	
	    	String error = "";
	    	//Fail
	    	Pattern patternError = Pattern.compile("MessageText:(.*?),");
	    	 Matcher matcherError = patternError.matcher(formatted);
			List<String> errors = new ArrayList<String>();
			matcherError.results().forEach(mat -> errors.add((mat.group(1))));
	    	System.out.println(error);
	    	log.error("Sending work order to SAP failed. (" + error +")");
	    
	    	workOrderRepo.delete(wo);
	    	
	    	sapResponse.setSuccess(false);
	    	sapResponse.setMessage(errors);
	    	
	    	log.info("Insert work order into db failed");
	
	    }
		
		return copy;
	}
	

	public CloseWorkOrderResponse closeWorkOrder(WorkOrderDTO workOrderDTO) throws Exception {
			log.info("Work order closing started");
			
			updateWorkOrder(workOrderDTO);
			
			CloseWorkOrderResponse closeWorkOrder = new CloseWorkOrderResponse();
			
			WorkOrder workOrder = workOrderRepo.getOne(workOrderDTO.getId());
			workOrder.setTreated(workOrderDTO.getTreated());
			workOrderRepo.save(workOrder);
			CloseWorkOrderDTO closeWorkORder = new CloseWorkOrderDTO(workOrder);
			if(workOrderDTO.isCancellation()) {
				closeWorkORder.setCancellation("X");
			} 
			System.out.println("CLOSE WO => " + closeWorkORder);
			Map<String, String> headerValues = getHeaderValuesClose();
			String csrfToken = headerValues.get("csrf");
			String authHeader = headerValues.get("authHeader");
			String cookies = headerValues.get("cookies");
		    
		    log.info("Sending work order to SAP started");
		    /*ResponseEntity<?> response = sap4hana.closeWorkOrder(cookies,
																"Basic " + authHeader, 
																csrfToken,
																"XMLHttpRequest",
																closeWorkORder);
		    */
		    HttpHeaders headersRestTemplate = new HttpHeaders();
	  		headersRestTemplate.set("Authorization", "Basic " + authHeader);
	  		headersRestTemplate.set("X-CSRF-Token", csrfToken);
	  		headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
	  		headersRestTemplate.set("Cookie", cookies);
	  		
	  		
	  		HttpEntity entity = new HttpEntity(closeWorkORder, headersRestTemplate);

	  		ResponseEntity<Object> response = restTemplate.postForEntity(
	  		    sapS4Close, entity, Object.class);
	  		
		    if(response == null) {
				throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		    }
		    String json = response.getBody().toString();
		    System.out.println(json + "AAA");
			
		    boolean flagErr = false;
		    Pattern pattern = Pattern.compile("ReturnStatus=(E|S),");
			if(json.contains("ReturnStatus=E")) {
				System.out.println("ASDFASFASFASFASF");
				flagErr = true;
			}
		    Matcher matcher = pattern.matcher(json);
			String flag = "";
			if(matcher.matches()){
				System.out.println("IMA GA");
				flag = matcher.group(1);
			}
			
			if(flagErr){
				
				System.out.println("USAO U ERR");
				Pattern patternMessage = Pattern.compile("MessageText=(.*?),");
				Matcher matcherMessage = patternMessage.matcher(json);
				
				matcherMessage.results().forEach(mat -> closeWorkOrder.getErrors().add((mat.group(1))));
				closeWorkOrder.setStatus(false);
				return closeWorkOrder;
			}else if(!flagErr) {
				System.out.println("USAO U SUCES");
				closeWorkOrder.setStatus(true);
				//this.updateWorkOrder(workOrderDTO);
				workOrder.setTreated(workOrderDTO.getTreated());
				if(workOrderDTO.isCancellation()) {
					workOrder.setStatus(WorkOrderStatus.CANCELLATION);
				} else {
					workOrder.setStatus(WorkOrderStatus.CLOSED);
					workOrder.setClosed(true);
				}
				workOrderRepo.save(workOrder);
				
				return closeWorkOrder;
			}else {
				throw new Exception("Greska prilikom zatvaranja naloga.");
			}
		    
			//this.updateWorkOrder(workOrderDTO);
			//workOrder.setTreated(workOrderDTO.getTreated());
			//workOrder.setStatus(WorkOrderStatus.CLOSED);
			//workOrder.setClosed(true);
			//workOrderRepo.save(workOrder);
	}
	
	public List<WorkOrderDTO> getAllByStatus(Long tenantId, WorkOrderStatus status){
		List<WorkOrder> workOrders = workOrderRepo.findWoByStatus(tenantId, status.toString());
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
		json = json.replaceAll(" NoteHeader:[a-zA-Z \\-0-9!_.,%?\\/()\\\\šŠćĆčČđĐŽž]*,", "");
		json = json.replaceAll(" NoteItem:[a-zA-Z \\-0-9!_.,%?\\/()\\\\šŠćĆčČđĐŽž]*,", "");
		//System.out.println(json);
		
		return json;

	}
	
	public Map<String, String> getHeaderValues(WorkOrder wo) throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder()
				.append("MKATIC")
				.append(":")
				.append("katicm0908");
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		
		//Testing HTTPS with RestTemplate
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");
		
		HttpEntity entity = new HttpEntity(headersRestTemplate);

		ResponseEntity<Object> response = null;
		try {
			response = restTemplate.exchange(
				    sapS4Hurl, HttpMethod.GET, entity, Object.class);
		} catch(Exception e) {
			workOrderRepo.delete(wo);
		}
		
		
		//ResponseEntity<Object> resp = sap4hana.getCSRFToken("Basic " + authHeader, "Fetch");
		
		if(response == null) {
			workOrderRepo.delete(wo);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		
		log.info("Getting X-CSRF-Token successfuly finished");
		
		HttpHeaders headers = response.getHeaders();
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
	
	public Map<String, String> getHeaderValues() throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder()
				.append("MKATIC")
				.append(":")
				.append("katicm0908");
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		//Testing HTTPS with RestTemplate
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");
		
		HttpEntity entity = new HttpEntity(headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.exchange(
		    sapS4Close, HttpMethod.GET, entity, Object.class);
		log.info("Getting X-CSRF-Token successfuly finished");
		
		HttpHeaders headers = response.getHeaders();
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
	
	public Map<String, String> getHeaderValuesClose() throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder()
				.append("MKATIC")
				.append(":")
				.append("katicm0908");
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");
		
		HttpEntity entity = new HttpEntity(headersRestTemplate);
		ResponseEntity<Object> resp = restTemplate.exchange(
			    sapS4Hurl, HttpMethod.GET, entity, Object.class);
		//ResponseEntity<Object> resp = sap4hana.getCSRFTokenClose("Basic " + authHeader, "Fetch");
		
		if(resp == null) {
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
		}
		
		log.info("Getting X-CSRF-Token successfuly finished");
		
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
	
	public List<WorkOrderDTO> getAllForTractorDriver(Long tenantId){
		List<WorkOrder> workOrders = workOrderRepo.findAllOrderByCreationDate();
		List<WorkOrderDTO> workOrdersDTO = new ArrayList<WorkOrderDTO>();
		
		for(WorkOrder workOrder : workOrders) {
			if(tenantId == workOrder.getTenantId()) {
				WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
				workOrdersDTO.add(workOrderDTO);
			}
		}
		return workOrdersDTO;
	}
	
	public List<WorkOrderDTO> getMyWoByStatus(Long sapUserId, Long tenantId, WorkOrderStatus status){
		System.out.println(tenantId + " -> " + sapUserId + " -> " + status);
		List<WorkOrder> workOrders = workOrderRepo.findMyWoByStatus(tenantId, sapUserId, status.toString());
		List<WorkOrderDTO> workOrdersDTO = new ArrayList<WorkOrderDTO>();
		for(WorkOrder workOrder : workOrders) {
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			workOrdersDTO.add(workOrderDTO);
		}
		System.out.println(workOrdersDTO);
		return workOrdersDTO;
	}
	
	public List<OperationsTodayDTO> getOperationsForToday(Long tenantId){
		List<OperationsTodayDTO> retValues = workOrderRepo.findAllOperationsForToday(tenantId);
		return retValues;
	}
	
}