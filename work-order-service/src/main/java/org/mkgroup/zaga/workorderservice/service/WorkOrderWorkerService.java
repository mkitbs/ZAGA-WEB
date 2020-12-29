package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.dto.MaterialReportDTO;
import org.mkgroup.zaga.workorderservice.dto.MaterialReportSumDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.UserAuthDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerTimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderTractorDriverDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerReportDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.mkgroup.zaga.workorderservice.feign.SAP4HanaProxy;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorkerStatus;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class WorkOrderWorkerService {
	
	private static final Logger log = Logger.getLogger(WorkOrderWorkerService.class);
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	OperationRepository operationRepo;
	
	@Autowired
	MachineRepository machineRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	SAP4HanaProxy sap4hana;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${sap.services.s4h}")
	String sapS4Hurl;
	
	
	public WorkOrderWorker getOne(UUID id) {
		try {
			WorkOrderWorker wow = wowRepo.getOne(id);
			return wow;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteWow(UUID id) {
		try {
			wowRepo.deleteWorker(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateWorkOrder(UUID id, WorkOrderWorkerDTO wowDTO) throws Exception {
		System.out.println(id);
		WorkOrderWorker wow = wowRepo.getOne(id);
		WorkOrder workOrder = wow.getWorkOrder();
		if(wowDTO.getDayPeriod() != null) {
			wow.setDayPeriod(wowDTO.getDayPeriod());
		} else {
			wow.setDayPeriod(-1.0);
		}
		if(wowDTO.getNightPeriod() != null) {
			wow.setNightPeriod(wowDTO.getNightPeriod());
		} else {
			wow.setNightPeriod(-1.0);
		}
		if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
			wow.setWorkPeriod(wowDTO.getDayPeriod() + wowDTO.getNightPeriod());
		} else {
			wow.setWorkPeriod(-1.0);
		}
		if(wowDTO.getInitialState() != null) {
			wow.setInitialState(wowDTO.getInitialState());
		} else {
			wow.setInitialState(-1.0);
		}
		if(wowDTO.getFinalState() != null) {
			wow.setFinalState(wowDTO.getFinalState());
		} else {
			wow.setFinalState(-1.0);
		}
		if(wowDTO.getFinalState() != null && wowDTO.getInitialState() != null) {
			wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		} else {
			wow.setSumState(-1.0);
		}
		if(wowDTO.getFuel() != null) {
			wow.setFuel(wowDTO.getFuel());
		} else {
			wow.setFuel(-1.0);
		}
		wow.setUser(userRepo.getOne(wowDTO.getUser().getUserId()));
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		if(wowDTO.getConnectingMachine().getDbid().equals("-1")) {
			wow.setConnectingMachine(null);
		}else {
			wow.setConnectingMachine(machineRepo.getOne(UUID.fromString(wowDTO.getConnectingMachine().getDbid())));
		}
		
		wow.setMachine(machineRepo.getOne(UUID.fromString(wowDTO.getMachine().getDbid())));
		
		wow = wowRepo.save(wow);
		
		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");
		
		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");
		
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
  		
  		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

  		ResponseEntity<Object> response = restTemplate.exchange(
  		    sapS4Hurl, HttpMethod.POST, entity, Object.class);
	
		System.out.println(response);
	    if(response == null) {
	    	wowRepo.delete(wow);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    System.out.println("REZZ" + response.getBody());
	    
	    String oDataString = response.getBody().toString().replace(":", "-");
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
	    }else if(status.equals("E")){
	    	System.out.println("ERROR");
	    	throw new Exception("Greska prilikom komunikacije sa SAP-om.");
	    }
	}
	
	public void updateWOWBasicInfo(UUID id, WorkOrderWorkerDTO wowDTO) throws Exception {
		WorkOrderWorker wow = wowRepo.getOne(id);
		WorkOrder workOrder = wow.getWorkOrder();
		wow.setUser(userRepo.getOne(wowDTO.getUser().getUserId()));
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		if(wowDTO.getConnectingMachine().getDbid().equals("-1")) {
			wow.setConnectingMachine(null);
		}else {
			wow.setConnectingMachine(machineRepo.getOne(UUID.fromString(wowDTO.getConnectingMachine().getDbid())));
		}
		
		wow.setMachine(machineRepo.getOne(UUID.fromString(wowDTO.getMachine().getDbid())));
		
		wow = wowRepo.save(wow);
		
		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");
		
		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");
		
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
  		
  		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

  		ResponseEntity<Object> response = restTemplate.exchange(
  		    sapS4Hurl, HttpMethod.POST, entity, Object.class);
	
		System.out.println(response);
	    if(response == null) {
	    	wowRepo.delete(wow);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    System.out.println("REZZ" + response.getBody());
	    
	    String oDataString = response.getBody().toString().replace(":", "-");
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
	    }else if(status.equals("E")){
	    	System.out.println("ERROR");
	    	throw new Exception("Greska prilikom komunikacije sa SAP-om.");
	    }
	}
	
	public void addWorker(UUID id, WorkOrderWorkerDTO wowDTO) throws Exception {
		WorkOrder workOrder = workOrderRepo.getOne(id);
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
		if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
			wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
		} else {
			wow.setWorkPeriod(-1.0);
		}
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		wow.setMachine(machineRepo.getOne(UUID.fromString(wowDTO.getMachine().getDbid())));
		if(wowDTO.getConnectingMachine().getDbid().equals("-1")) {
			wow.setConnectingMachine(null);
		}else {
			wow.setConnectingMachine(machineRepo.getOne(UUID.fromString(wowDTO.getConnectingMachine().getDbid())));
		}
		if(wowDTO.getInitialState() != null) {
			wow.setInitialState(wowDTO.getInitialState());
		} else {
			wow.setInitialState(-1.0);
		}
		if(wowDTO.getFinalState() != null) {
			wow.setFinalState(wowDTO.getFinalState());
		} else {
			wow.setFinalState(-1.0);
		}
		if(wowDTO.getInitialState() != null && wowDTO.getFinalState() != null) {
			wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		} else {
			wow.setSumState(-1.0);
		}
		wow.setWorkOrder(workOrder);
		wow.setUser(userRepo.getOne(wowDTO.getUser().getUserId()));

		wow = wowRepo.save(wow);
		
		Map<String, String> headerValues = getHeaderValues();
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");
		
		WorkOrderToSAP workOrderSAP = new WorkOrderToSAP(workOrder, "MOD");
		
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
  		
  		HttpEntity entity = new HttpEntity(workOrderSAP, headersRestTemplate);

  		ResponseEntity<Object> response = restTemplate.exchange(
  		    sapS4Hurl, HttpMethod.POST, entity, Object.class);
		
		System.out.println(response);
	    if(response == null) {
	    	wowRepo.delete(wow);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    System.out.println("REZZ" + response.getBody());
	    
	    String oDataString = response.getBody().toString().replace(":", "-");
	    String formatted = formatJSON(oDataString);
	    JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
	    JsonArray array = convertedObject.get("d").getAsJsonObject().get("WorkOrderToEmployeeNavigation").getAsJsonObject().get("results").getAsJsonArray();
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
	    	for(int i = 0; i <array.size(); i++) {
	    		UUID uid = UUID.fromString(array.get(i).getAsJsonObject().get("WebBackendId").getAsString());
	    		WorkOrderWorker worker = wowRepo.getOne(uid);
	    		
	    		worker.setErpId(array.get(i).getAsJsonObject().get("WorkOrderEmployeeNumber").getAsInt());
	    		wowRepo.save(worker);
	    	}
	    }else if(status.equals("E")){
	    	System.out.println("ERROR");
	    	throw new Exception("Greska prilikom komunikacije sa SAP-om.");
	    }
		
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
		
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");
		
		HttpEntity entity = new HttpEntity(headersRestTemplate);

		ResponseEntity<Object> resp = restTemplate.exchange(
		    sapS4Hurl, HttpMethod.GET, entity, Object.class);

		
		//ResponseEntity<Object> resp = sap4hana.getCSRFToken("Basic " + authHeader, "Fetch");
		
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
	
	public List<WorkerReportDTO> getWorkersForReport(String sapUserId){
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
		
		List<WorkOrderWorker> wows = wowRepo.findAllByOrderByWorkerId(user.getBody().getTenant().getId());
		WorkerReportDTO report = new WorkerReportDTO();
		List<WorkerReportDTO> retValues = new ArrayList<WorkerReportDTO>();
		if(wows.size() > 0) {
			report.setWorker(new EmployeeDTO(wows.get(0).getUser()));
			report.getWorkOrders().add(new WorkOrderDTO(wows.get(0).getWorkOrder(), wows.get(0).getUser().getPerNumber()));
			
			if(wows.size() == 1) {
				retValues.add(report);
			}
		}
		for(int i = 0; i<wows.size()-1; i++) {
			if(wows.get(i).getUser().getId().equals(wows.get(i+1).getUser().getId())) {
				if(!wows.get(i).getWorkOrder().equals(wows.get(i+1).getWorkOrder())){
					report.getWorkOrders().add(new WorkOrderDTO(wows.get(i+1).getWorkOrder(), wows.get(i+1).getUser().getPerNumber()));
				}
				
				if(i+1 == wows.size()-1) {
					retValues.add(report);
				}
			} else {
				retValues.add(report);
				report = new WorkerReportDTO();
				report.setWorker(new EmployeeDTO(wows.get(i+1).getUser()));
				report.getWorkOrders().add(new WorkOrderDTO(wows.get(i+1).getWorkOrder(), wows.get(i+1).getUser().getPerNumber()));
				
				if(i+1 == wows.size()-1) {
					retValues.add(report);
				}
			}
			
		}
		
		for(WorkerReportDTO w : retValues) {
			double dayPeriodSum = 0.0;
			double nightPeriodSum = 0.0;
			double workPeriodSum = 0.0;
			for(WorkOrderDTO wo : w.getWorkOrders()) {
				for(WorkOrderWorkerDTO wow : wo.getWorkers()) {
					if(wow.getDayPeriod() == -1) {
						wow.setDayPeriod(0.0);
					}
					dayPeriodSum += wow.getDayPeriod();
					if(wow.getNightPeriod() == -1) {
						wow.setNightPeriod(0.0);
					}
					nightPeriodSum += wow.getNightPeriod();
					if(wow.getWorkPeriod() == -1) {
						wow.setWorkPeriod(0.0);
					}
					workPeriodSum += wow.getWorkPeriod();
				}
			}
			w.setDayPeriodSum(dayPeriodSum);
			w.setNightPeriodSum(nightPeriodSum);
			w.setWorkPeriodSum(workPeriodSum);
		}
		
		return retValues;
	}
	
	public List<WorkOrderTractorDriverDTO> getWorkOrdersForTractorDriver(UUID workerId){
		List<WorkOrderWorker> result = wowRepo.findAllWoWByWorker(workerId);
		List<WorkOrderTractorDriverDTO> retValues = new ArrayList<WorkOrderTractorDriverDTO>();
		for(WorkOrderWorker wow : result) {
			if(!wow.getMachine().getErpId().equals("BEZ-MASINE") && !wow.getStatus().equals(WorkOrderWorkerStatus.FINISHED)) {
				if(wow.getStatus().equals(WorkOrderWorkerStatus.STARTED) || wow.getStatus().equals(WorkOrderWorkerStatus.PAUSED)){
					WorkOrderTractorDriverDTO wowDTO = new WorkOrderTractorDriverDTO(wow, true);
					retValues.add(wowDTO);
				} else {
					WorkOrderTractorDriverDTO wowDTO = new WorkOrderTractorDriverDTO(wow, false);
					retValues.add(wowDTO);
				}
				
			}
		}
		return retValues;
	}
	
}
