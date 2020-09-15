package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.OperationDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OperationService {
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	OperationRepository operationRepo;
	
	public List<OperationDTO> getOperationsFromSAP() throws JSONException {
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		//Call SAP and retrieve cultureGroupSet
		ResponseEntity<Object> cultureGroupSet = 
				gwProxy.fetchOperations("json", "Basic " + authHeader);
		String oDataString = cultureGroupSet.getBody().toString().replace(":", "-");
		System.out.println(oDataString);
		oDataString = formatJSON(oDataString);
		System.out.println(oDataString);
		//Map to specific object
	    ArrayList<OperationDTO> operationList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));

	    for(OperationDTO op: operationList) {
	    	Operation operation = new Operation(op);
	    	operationRepo.save(operation);
	    }
		return operationList;
	}
	
	public ArrayList<OperationDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    ArrayList<OperationDTO> convertedList= new ArrayList<OperationDTO>();
	    list.forEach(objectOfAList -> {
	    	OperationDTO cultureDTO = new OperationDTO();
	    	
			try {
				cultureDTO = objectMapper
											.readValue(objectOfAList.toString(),
													OperationDTO.class);
				convertedList.add(cultureDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    });
	    return convertedList;
	}
	
	public Operation getOne(UUID id) {
		try {
			Operation operation = operationRepo.getOne(id);
			return operation;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
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
