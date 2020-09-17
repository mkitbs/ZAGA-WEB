package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.CropVarietyDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.CropVariety;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.CropVarietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CropVarietyService {

	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	CropVarietyRepository cropVarietyRepo;
	
	public List<CropVarietyDTO> getCropVarietiesFromSAP() throws JSONException {
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		//Call SAP and retrieve cropVarietySet
		ResponseEntity<Object> cropVarietySet = 
				gwProxy.fetchCropVarieties("json", "Basic " + authHeader);
		String oDataString = cropVarietySet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		
		//Map to specific object
	    ArrayList<CropVarietyDTO> cropVarietyList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));
	    
	    for(CropVarietyDTO cropVariety : cropVarietyList) {
	    	CropVariety cv = new CropVariety(cropVariety);
	    	cropVarietyRepo.save(cv);
	    }

		return cropVarietyList;
	}
	
	public ArrayList<CropVarietyDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<CropVarietyDTO> convertedList= new ArrayList<CropVarietyDTO>();
	    list.forEach(objectOfAList -> {
	    	CropVarietyDTO cropVarietyDTO = new CropVarietyDTO();
	    	
			try {
				cropVarietyDTO = objectMapper
											.readValue(objectOfAList.toString(),
													CropVarietyDTO.class);
				convertedList.add(cropVarietyDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    });
	    return convertedList;
	}

	public String formatJSON(String json) {
		json = json.replace("=", ":");
		json = json.replaceAll("__metadata:\\{[a-zA-Z0-9,':=\".()/_ -]*\\},", "");
		json = json.replace("/", "");
		json = json.replaceAll(":,", ":\"\",");
		json = json.replaceAll(":}", ":\"\"}");
		return json;
	}
	
}
