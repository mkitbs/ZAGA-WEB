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
		oDataString = oDataString.replace("=", ":");
		oDataString = oDataString.replace("/", "");
		//Map to specific object
	    ArrayList<OperationDTO> cultureGroupList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));

		return cultureGroupList;
	}
	
	public ArrayList<OperationDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
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


}
