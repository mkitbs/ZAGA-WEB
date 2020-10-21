package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.OperationGroupDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.OperationGroup;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.OperationGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OperationGroupService {
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	OperationGroupRepository operationGroupRepo;
	
	public List<OperationGroupDTO> getOperationGroupsFromSAP() throws JSONException {
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		//Call SAP and retrieve operationGroupSet
		ResponseEntity<Object> operationGroupSet = 
				gwProxy.fetchOperationGroups("json", "Basic " + authHeader);
		String oDataString = operationGroupSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		//Map to specific object
	    ArrayList<OperationGroupDTO> operationGroupList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));
	    
	    for(OperationGroupDTO operation : operationGroupList) {
	    	operationGroupRepo
	    			.findByErpId(operation.getId())
	    			.ifPresentOrElse(foundOperationGroup -> updateOperationGroup(foundOperationGroup, operation),
	    							() -> createOperationGroup(operation));
	    }

		return operationGroupList;
	}

	public ArrayList<OperationGroupDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<OperationGroupDTO> convertedList= new ArrayList<OperationGroupDTO>();
	    list.forEach(objectOfAList -> {
	    	OperationGroupDTO operationDTO = new OperationGroupDTO();
	    	
			try {
				operationDTO = objectMapper
											.readValue(objectOfAList.toString(),
													OperationGroupDTO.class);
				convertedList.add(operationDTO);
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
	
	private void createOperationGroup(OperationGroupDTO operation) {
		OperationGroup op = new OperationGroup(operation);
    	operationGroupRepo.save(op);
	}

	private void updateOperationGroup(OperationGroup oldOperationGroup, OperationGroupDTO updatedOperationGroup) {
		oldOperationGroup.setName(updatedOperationGroup.getName());
		operationGroupRepo.save(oldOperationGroup);
	}
	
	public List<OperationGroupDTO> getAll(){
		List<OperationGroupDTO> retValues = new ArrayList<OperationGroupDTO>();
		List<OperationGroup> operationGroups = operationGroupRepo.findAll();
		for(OperationGroup operationGroup : operationGroups) {
			OperationGroupDTO retValue = new OperationGroupDTO(operationGroup);
			retValues.add(retValue);
		}
		return retValues;
	}

}
