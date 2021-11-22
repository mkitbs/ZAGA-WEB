package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.dto.MaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.MaterialReportDTO;
import org.mkgroup.zaga.workorderservice.dto.MaterialReportHelperDTO;
import org.mkgroup.zaga.workorderservice.dto.MaterialReportSumDTO;
import org.mkgroup.zaga.workorderservice.dto.Response;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialPerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialPerCultureReportDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialsDTO;
import org.mkgroup.zaga.workorderservice.dto.UserAuthDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderForMaterialReportDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.mkgroup.zaga.workorderservice.feign.SAP4HanaProxy;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessResourceFailureException;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

@Service
public class SpentMaterialService {
	
	private static final Logger log = Logger.getLogger(SpentMaterial.class);

	@Autowired
	SpentMaterialRepository spentMaterialRepo;
	
	@Autowired
	MaterialRepository materialRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	SAP4HanaProxy sap4hana;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${sap.services.s4h}")
	String sapS4Hurl;
	
	public SpentMaterial addSpentMaterial(SpentMaterial sm) {
		try {
			log.info("Insert spent material into db.");
			sm = spentMaterialRepo.save(sm);
			return sm;
		} catch(Exception e) {
			log.error("Insert spent material faild.", e);
			return null;
		}
	}
	
	public SpentMaterial getOne(UUID id) {
		try {
			SpentMaterial spentMaterial = spentMaterialRepo.getOne(id);
			return spentMaterial;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteSpentMaterial(UUID id) {
		try {
			spentMaterialRepo.deleteMaterial(id);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addSpentMaterial(UUID id, SpentMaterialDTO spentMaterialDTO) throws Exception {
		WorkOrder workOrder = workOrderRepo.getOne(id);
				
		SpentMaterial spentMaterial = new SpentMaterial();
		spentMaterial.setWorkOrder(workOrder);
		if(spentMaterialDTO.getQuantity() != null) {
			spentMaterial.setQuantity(spentMaterialDTO.getQuantity());
			spentMaterial.setQuantityPerHectar(spentMaterialDTO.getQuantity() / workOrder.getCrop().getArea());
		} else {
			spentMaterial.setQuantity(-1.0);
			spentMaterial.setQuantityPerHectar(-1.0);
		}
		if(spentMaterialDTO.getSpent() != null) {
			spentMaterial.setSpent(spentMaterialDTO.getSpent());
			spentMaterial.setSpentPerHectar(spentMaterialDTO.getSpent() / workOrder.getCrop().getArea());
		} else {
			spentMaterial.setSpent(-1.0);
			spentMaterial.setSpentPerHectar(-1.0);
		}
		
		
		Material material = materialRepo.getOne(spentMaterialDTO.getMaterial().getDbid());
		spentMaterial.setMaterial(material);
		
		spentMaterial = spentMaterialRepo.save(spentMaterial);
		
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
	    	spentMaterialRepo.delete(spentMaterial);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    System.out.println("REZZ" + response.getBody());
	    
	    String oDataString = response.getBody().toString().replace(":", "-");
	    String formatted = formatJSON(oDataString);
	    JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
	    JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();
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
	    	for(int i = 0; i <arrayMaterial.size(); i++) {
	    		UUID uid = UUID.fromString(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString());
	    		SpentMaterial spentMat = spentMaterialRepo.getOne(uid);
	    		
	    		spentMat.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
	    		spentMaterialRepo.save(spentMat);
	    	}
	    }else if(status.equals("E")){
	    	spentMaterial.setDeleted(true);
	    	spentMaterialRepo.save(spentMaterial);
	    	System.out.println("ERROR");
	    	throw new Exception("Greska prilikom komunikacije sa SAP-om.");
	    }
		
		
	}
	
	public Response updateSpentMaterial(UUID id, SpentMaterialDTO spentMaterialDTO) throws Exception {
		SpentMaterial spentMaterial = spentMaterialRepo.getOne(id);
		WorkOrder workOrder = workOrderRepo.getOne(spentMaterial.getWorkOrder().getId());
		
		Response updateResponse = new Response();
		
		spentMaterial.setQuantity(spentMaterialDTO.getQuantity());
		spentMaterial.setQuantityPerHectar(spentMaterialDTO.getQuantity() / workOrder.getCrop().getArea());
		if(spentMaterialDTO.getSpent() != null) {
			spentMaterial.setSpent(spentMaterialDTO.getSpent());
			spentMaterial.setSpentPerHectar(spentMaterialDTO.getSpent() / workOrder.getCrop().getArea());
		} else {
			spentMaterial.setSpent(-1.0);
			spentMaterial.setSpentPerHectar(-1.0);
		}
		
		Material material = materialRepo.getOne(spentMaterialDTO.getMaterial().getDbid());
		spentMaterial.setMaterial(material);
		
		spentMaterial = spentMaterialRepo.save(spentMaterial);
		
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
	    	spentMaterialRepo.delete(spentMaterial);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    System.out.println("REZZ" + response.getBody());
	    
	    /*String oDataString = response.toString().replace(":", "-");
	    String formatted = formatJSON(oDataString);
	    JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
	    JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();
	    System.out.println(formatted + "ASASA");*/
	    String resp = response.getBody().toString();
	    Pattern pattern = Pattern.compile("ReturnStatus=(.*?),");
		Matcher matcher = pattern.matcher(resp);
		String status = "";
		if (matcher.find())
		{
		    status = matcher.group(1);
		    System.out.println("NASAO STATUS " + status);
		}
		
	    
	    if(status.equals("S")) {
	    	System.out.println("USPESNO DODAT");
	    	String oDataString = response.getBody().toString().replace(":", "-");
	    	String formatted = formatJSON(oDataString);
		    JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
		    JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();
	    	for(int i = 0; i <arrayMaterial.size(); i++) {
	    		UUID uid = UUID.fromString(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString());
	    		SpentMaterial spentMat = spentMaterialRepo.getOne(uid);
	    		
	    		spentMat.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
	    		spentMaterialRepo.save(spentMat);
	    	}
	    	updateResponse.setSuccess(true);
	    	return updateResponse;
	    }else if(status.equals("E")){
	    	Pattern patternError = Pattern.compile("MessageText=(.*?),");
			Matcher matcherError = patternError.matcher(resp);
			List<String> errors = new ArrayList<String>();
			matcherError.results().forEach(mat -> errors.add((mat.group(1))));
	    	System.out.println("ERROR");
	    	updateResponse.setErrors(errors);
	    	updateResponse.setSuccess(false);
	    	spentMaterial.setSpent(-1.0);
	    	spentMaterialRepo.save(spentMaterial);
	    	return updateResponse;
	    }
		
		updateResponse.setSuccess(false);
		return updateResponse;
	}
	
	public Response updateMaterialBasicInfo(UUID id, SpentMaterialDTO spentMaterialDTO) throws Exception {
		SpentMaterial spentMaterial = spentMaterialRepo.getOne(id);
		WorkOrder workOrder = workOrderRepo.getOne(spentMaterial.getWorkOrder().getId());
		
		Response updateResponse = new Response();
		
		spentMaterial.setQuantity(spentMaterialDTO.getQuantity());
		spentMaterial.setQuantityPerHectar(spentMaterialDTO.getQuantity() / workOrder.getCrop().getArea());
		
		Material material = materialRepo.getOne(spentMaterialDTO.getMaterial().getDbid());
		spentMaterial.setMaterial(material);
		
		spentMaterial = spentMaterialRepo.save(spentMaterial);
		
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
	    	spentMaterialRepo.delete(spentMaterial);
			throw new Exception("Greska prilikom konekcije na SAP. Morate biti konektovani na VPN.");
	    }
	    System.out.println("REZZ" + response.getBody());
	    
	    /*String oDataString = response.toString().replace(":", "-");
	    String formatted = formatJSON(oDataString);
	    JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
	    JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();
	    System.out.println(formatted + "ASASA");*/
	    String resp = response.getBody().toString();
	    Pattern pattern = Pattern.compile("ReturnStatus=(.*?),");
		Matcher matcher = pattern.matcher(resp);
		String status = "";
		if (matcher.find())
		{
		    status = matcher.group(1);
		    System.out.println("NASAO STATUS " + status);
		}
		
	    
	    if(status.equals("S")) {
	    	System.out.println("USPESNO DODAT");
	    	String oDataString = response.getBody().toString().replace(":", "-");
	    	String formatted = formatJSON(oDataString);
		    JsonObject convertedObject = new Gson().fromJson(formatted, JsonObject.class);
		    JsonArray arrayMaterial = convertedObject.get("d").getAsJsonObject().get("WorkOrderToMaterialNavigation").getAsJsonObject().get("results").getAsJsonArray();
	    	for(int i = 0; i <arrayMaterial.size(); i++) {
	    		UUID uid = UUID.fromString(arrayMaterial.get(i).getAsJsonObject().get("WebBackendId").getAsString());
	    		SpentMaterial spentMat = spentMaterialRepo.getOne(uid);
	    		
	    		spentMat.setErpId(arrayMaterial.get(i).getAsJsonObject().get("WorkOrderMaterialNumber").getAsInt());
	    		spentMaterialRepo.save(spentMat);
	    	}
	    	updateResponse.setSuccess(true);
	    	return updateResponse;
	    }else if(status.equals("E")){
	    	Pattern patternError = Pattern.compile("MessageText=(.*?),");
			Matcher matcherError = patternError.matcher(resp);
			List<String> errors = new ArrayList<String>();
			matcherError.results().forEach(mat -> errors.add((mat.group(1))));
	    	System.out.println("ERROR");
	    	updateResponse.setErrors(errors);
	    	updateResponse.setSuccess(false);
	    	spentMaterial.setQuantity(-1.0);
	    	spentMaterial.setQuantityPerHectar(-1.0);
	    	spentMaterialRepo.save(spentMaterial);
	    	return updateResponse;
	    }
		
		updateResponse.setSuccess(false);
		return updateResponse;
	}
	
	public Map<String, String> getHeaderValues() throws Exception {
		log.info("Getting X-CSRF-Token started");
		StringBuilder authEncodingString = new StringBuilder()
				.append("LJKOMNENOVIC")
				.append(":")
				.append("Ljubica456");
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
		json = json.replaceAll(" NoteHeader:[a-zA-Z \\-0-9!_.,%?\\/()\\\\šŠćĆčČđĐŽž]*,", "");
		json = json.replaceAll(" NoteItem:[a-zA-Z \\-0-9!_.,%?\\/()\\\\šŠćĆčČđĐŽž]*,", "");
		//System.out.println(json);
		
		return json;

	}
	
	public List<MaterialReportDTO> getMaterialsForReport(Long tenantId){
		
		List<SpentMaterial> spentMaterials = spentMaterialRepo.findAllByOrderByMaterialId(tenantId);
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setAmbiguityIgnored(true);
		List<MaterialReportDTO> retValues = new ArrayList<MaterialReportDTO>();
		for(SpentMaterial sm : spentMaterials) {
			MaterialReportDTO report = new MaterialReportDTO();
			List<WorkOrderForMaterialReportDTO> workOrders = workOrderRepo.findAllByMaterial(sm.getMaterial().getId(), tenantId);
			report.setMaterial(new SpentMaterialDTO(sm));
			report.setWorkOrders(workOrders);
			retValues.add(report);
		}
		
		return retValues;
	}
	
	public List<SpentMaterialPerCultureReportDTO> getSpentMaterialPerCulture(Long tenantId){
		List<SpentMaterialPerCultureDTO> spentMaterials = spentMaterialRepo.getSpentMaterialPerCulture(tenantId);
		SpentMaterialPerCultureReportDTO spentMatReport = new SpentMaterialPerCultureReportDTO();
		List<SpentMaterialPerCultureReportDTO> retValues = new ArrayList<SpentMaterialPerCultureReportDTO>();
		
		if(spentMaterials.size() > 0) {
			spentMatReport.setCrop(spentMaterials.get(0).getCrop());
			spentMatReport.setCulture(spentMaterials.get(0).getCulture());
			SpentMaterialsDTO material = new SpentMaterialsDTO(spentMaterials.get(0));
			List<SpentMaterialsDTO> materials = new ArrayList<SpentMaterialsDTO>();
			materials.add(material);
			spentMatReport.setSpentMaterials(materials);
			if(spentMaterials.size() == 1) {
				retValues.add(spentMatReport);
			}
		}
		
		for(int i = 0; i<spentMaterials.size()-1; i++) {
			List<SpentMaterialsDTO> materials = new ArrayList<SpentMaterialsDTO>();
			if(spentMaterials.get(i).getCrop().equals(spentMaterials.get(i+1).getCrop())) {
				spentMatReport.getSpentMaterials().add(new SpentMaterialsDTO(spentMaterials.get(i+1)));
				if(i+1 == spentMaterials.size()-1) {
					retValues.add(spentMatReport);
				}
			} else {
				retValues.add(spentMatReport);
				spentMatReport = new SpentMaterialPerCultureReportDTO();
				spentMatReport.setCrop(spentMaterials.get(i+1).getCrop());
				spentMatReport.setCulture(spentMaterials.get(i+1).getCulture());
				SpentMaterialsDTO material = new SpentMaterialsDTO(spentMaterials.get(i+1));
				materials.add(material);
				spentMatReport.setSpentMaterials(materials);
				if(i+1 == spentMaterials.size()-1) {
					retValues.add(spentMatReport);
				}
			}
			
		}
		return retValues;
	}
	
}
