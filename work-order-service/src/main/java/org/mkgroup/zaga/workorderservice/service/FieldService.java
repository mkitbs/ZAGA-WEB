package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.FieldDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.Field;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FieldService {
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	FieldRepository fieldRepo;
	
	public List<FieldDTO> getFieldsFromSAP() throws JSONException{
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
			    authEncodingString.toString().getBytes());
		//Call SAP and retrieve fieldSet
		ResponseEntity<Object> fieldSet = 
				gwProxy.fetchFields("json", "Basic " + authHeader);
		String oDataString = fieldSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		//Map to specific object
		ArrayList<FieldDTO> fieldList = 
								convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));
		for(FieldDTO field : fieldList) {
			Field f = new Field(field);
			fieldRepo.save(f);
		}
		
		return fieldList;
	}

	private ArrayList<FieldDTO> convertObjectToLocalList(Object listAsObject) {
		List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<FieldDTO> convertedList = new ArrayList<FieldDTO>();
	    list.forEach(objectOfAList -> {
	    	FieldDTO fieldDTO = new FieldDTO();
	    	
	    	try {
	    		fieldDTO = objectMapper
	    										
	    									.readValue(objectOfAList.toString(),
	    												FieldDTO.class);
	    		convertedList.add(fieldDTO);
	    	} catch(Exception e) {
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
	
	public List<FieldDTO> getAll() {
		List<Field> fields = fieldRepo.findAll();
		List<FieldDTO> retValues = new ArrayList<FieldDTO>();
		for(Field field : fields) {
			FieldDTO f = new FieldDTO(field);
			retValues.add(f);
		}
		return retValues;
	}
}
