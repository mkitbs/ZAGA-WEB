package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.dto.ResponseTimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.dto.TimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.dto.UserAuthDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderTractorDriverDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerTimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.TimeTrackingToSAP;
import org.mkgroup.zaga.workorderservice.model.TimeTrackingType;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorkerStatus;
import org.mkgroup.zaga.workorderservice.model.WorkerTimeTracking;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkerTimeTrackingRepository;
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
public class WorkerTimeTrackingService {
	
	private static final Logger log = Logger.getLogger(WorkerTimeTrackingService.class);

	@Autowired
	WorkerTimeTrackingRepository timeTrackingRepo;
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	@Value("${sap.services.s4h_tracking}")
	String sapS4Hurl;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	public WorkerTimeTrackingDTO getOne(UUID wowId) {
		WorkOrderWorker wow = wowRepo.getOne(wowId);
		List<WorkerTimeTracking> times = timeTrackingRepo.findByWorkOrderWorkerId(wowId);
		WorkerTimeTrackingDTO retValue = new WorkerTimeTrackingDTO();
		boolean inProgress;
		if(wow.getStatus().equals(WorkOrderWorkerStatus.STARTED) || wow.getStatus().equals(WorkOrderWorkerStatus.PAUSED)) {
			inProgress = true;
		} else {
			inProgress = false;
		}
		retValue.setHeaderInfo(new WorkOrderTractorDriverDTO(wow, inProgress));
		List<TimeTrackingDTO> timesDTO = new ArrayList<TimeTrackingDTO>();
		for(WorkerTimeTracking time : times) {
			TimeTrackingDTO timeDTO = new TimeTrackingDTO(time);
			timesDTO.add(timeDTO);
		}
		retValue.setTimes(timesDTO);
		return retValue;
		
	}
	
	public List<TimeTrackingDTO> setTracking(TimeTrackingDTO timeTracking, String sapUserId) {
		WorkerTimeTracking wtt = new WorkerTimeTracking();
		WorkOrderWorker wo = wowRepo.getOne(timeTracking.getWowId());
		wtt.setWorkOrderWorker(wo);
		wtt.setStartTime(timeTracking.getStartTime());
		wtt.setEndTime(timeTracking.getEndTime());
		if(timeTracking.getType().equals("RN")) {
			wtt.setType(TimeTrackingType.RN);
			wo.setStatus(WorkOrderWorkerStatus.STARTED);
		}else if(timeTracking.getType().equals("PAUSE_WORK")) {
			wtt.setType(TimeTrackingType.PAUSE_WORK);
			wo.setStatus(WorkOrderWorkerStatus.PAUSED);
		}else if(timeTracking.getType().equals("PAUSE_SERVICE")) {
			wtt.setType(TimeTrackingType.PAUSE_SERVICE);
			wo.setStatus(WorkOrderWorkerStatus.PAUSED);
		}else if(timeTracking.getType().equals("PAUSE_FUEL")) {
			wtt.setType(TimeTrackingType.PAUSE_FUEL);
			wo.setStatus(WorkOrderWorkerStatus.PAUSED);
		}
		long id = 1;
		wtt.setErpId(id);
		wtt = timeTrackingRepo.save(wtt);
		try {
			sendTrackingToSAP(wtt, sapUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		wowRepo.save(wo);
		//ResponseTimeTrackingDTO retValue = new ResponseTimeTrackingDTO(wtt);
		List<TimeTrackingDTO> retValue = new ArrayList<TimeTrackingDTO>();
		for(WorkerTimeTracking wt : wo.getWorkersTimeTracking()) {
			TimeTrackingDTO tt = new TimeTrackingDTO(wt);
			retValue.add(tt);
		}
		return retValue;
	}
	
	public List<TimeTrackingDTO> updateTracking(TimeTrackingDTO timeTracking, String sapUserId) {
		WorkerTimeTracking wtt = timeTrackingRepo.getOne(timeTracking.getId());
		WorkOrderWorker wo = wowRepo.getOne(timeTracking.getWowId());
		wtt.setEndTime(timeTracking.getEndTime());
		if(timeTracking.getType().startsWith("PAUSE")) {
			wo.setStatus(WorkOrderWorkerStatus.STARTED);
		} else {
			wtt.setType(TimeTrackingType.FINISHED);
			wo.setStatus(WorkOrderWorkerStatus.FINISHED);
		}
		wtt = timeTrackingRepo.save(wtt);
		try {
			sendUpdateTrackingToSAP(wtt, sapUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		wowRepo.save(wo);
		List<TimeTrackingDTO> retValue = new ArrayList<TimeTrackingDTO>();
		for(WorkerTimeTracking wt : wo.getWorkersTimeTracking()) {
			TimeTrackingDTO tt = new TimeTrackingDTO(wt);
			retValue.add(tt);
		}
		return retValue;
	}
	
	public void sendTrackingToSAP(WorkerTimeTracking wtt, String sapUserId) throws Exception {
		//get logged user
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
		System.out.println(user.getBody());
		String companyCode = user.getBody().getTenant().getCompanyCode();
		
		TimeTrackingToSAP timeTracking = new TimeTrackingToSAP(wtt, companyCode);
		System.out.println("Time tracking to SAP(set) => " + timeTracking);
		
		Map<String, String> headerValues = getHeaderValues(wtt);
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");
	    
	    log.info("Sending time tracking to SAP started");
	    
	    HttpHeaders headersRestTemplate = new HttpHeaders();
			headersRestTemplate.set("Authorization", "Basic " + authHeader);
			headersRestTemplate.set("X-CSRF-Token", csrfToken);
			headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
			headersRestTemplate.set("Cookie", cookies);
			System.out.println("Token:" + csrfToken);
			System.out.println(headersRestTemplate.toString());
			HttpEntity entity = new HttpEntity(timeTracking, headersRestTemplate);

			ResponseEntity<Object> response = null;
			try {
				response = restTemplate.exchange(
			  		    sapS4Hurl, HttpMethod.POST, entity, Object.class);
			} catch(Exception e) {
				timeTrackingRepo.delete(wtt);
			}
			
			System.out.println("Rest Template Testing SAP: " + response.getBody().toString());

	    if(response == null) {
	    	timeTrackingRepo.delete(wtt);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    } else {
	    	String oDataString = response.getBody().toString().replace(":", "-");
		    String formatted = formatJSON(oDataString);
	    	JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
	    	//System.out.println("CONVERTED => " + convertedObject.getAsString());
	    	JsonObject obj = convertedObject.get("d").getAsJsonObject();
	    	wtt.setErpId(obj.get("RecordNumber").getAsLong());
	    	timeTrackingRepo.save(wtt);
	    }
	    
	}
	
	public void sendUpdateTrackingToSAP(WorkerTimeTracking wtt, String sapUserId) throws Exception {
		//get logged user
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
		System.out.println(user.getBody());
		String companyCode = user.getBody().getTenant().getCompanyCode();
		
		TimeTrackingToSAP timeTracking = new TimeTrackingToSAP(wtt, companyCode);
		System.out.println("Time tracking to SAP(update) => " + timeTracking);
		
		Map<String, String> headerValues = getHeaderValues(wtt);
		String csrfToken = headerValues.get("csrf");
		String authHeader = headerValues.get("authHeader");
		String cookies = headerValues.get("cookies");
	    
	    log.info("Sending time tracking to SAP started");
	    
	    HttpHeaders headersRestTemplate = new HttpHeaders();
			headersRestTemplate.set("Authorization", "Basic " + authHeader);
			headersRestTemplate.set("X-CSRF-Token", csrfToken);
			headersRestTemplate.set("X-Requested-With", "XMLHttpRequest");
			headersRestTemplate.set("Cookie", cookies);
			System.out.println("Token:" + csrfToken);
			System.out.println(headersRestTemplate.toString());
			HttpEntity entity = new HttpEntity(timeTracking, headersRestTemplate);

			ResponseEntity<Object> response = null;
			String keyForUpdate = String.format(
					"(CompanyCode='%s',WorkOrderNumber='%s',"
					+ "WorkOrderEmployeeNumber='%s',RecordNumber='%s')",
					timeTracking.getCompanyCode(), timeTracking.getWorkOrderNumber(),
					timeTracking.getWorkOrderEmployeeNumber(), timeTracking.getRecordNumber());
			String url = sapS4Hurl.concat(keyForUpdate);
			System.out.println("URL FOR UPDATE => " + url);
			
			try {
				response = restTemplate.exchange(
			  		    url, HttpMethod.PUT, entity, Object.class);
			} catch(Exception e) {
				timeTrackingRepo.delete(wtt);
			}
			
	  	
		/*System.out.println("Rest Template Testing SAP: " + response.getBody().toString());
	    
	    if(response == null) {
	    	timeTrackingRepo.delete(wtt);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }*/
	    
	}
	
	
	public Map<String, String> getHeaderValues(WorkerTimeTracking wtt) throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder()
				.append("LJKOMNENOVIC")
				.append(":")
				.append("Ljubica789");
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		
		//Testing HTTPS with RestTemplate
		HttpHeaders headersRestTemplate = new HttpHeaders();
		headersRestTemplate.set("Authorization", "Basic " + authHeader);
		headersRestTemplate.set("X-CSRF-Token", "Fetch");
		
		HttpEntity entity = new HttpEntity(headersRestTemplate);

		ResponseEntity<Object> response = restTemplate.exchange(
		    sapS4Hurl, HttpMethod.GET, entity, Object.class);
		
		if(response == null) {
			timeTrackingRepo.delete(wtt);
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
}
