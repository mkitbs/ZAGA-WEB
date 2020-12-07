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
import org.mkgroup.zaga.workorderservice.model.OperationGroup;
import org.mkgroup.zaga.workorderservice.model.OperationType;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.OperationGroupRepository;
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
	
	@Autowired
	OperationGroupRepository operationGroupRepo;
	
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
	    	operationRepo
	    		.findByErpId(op.getErpId())
	    		.ifPresentOrElse(foundOperation -> updateOperation(foundOperation, op), 
	    						() -> createOperation(op));
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
	    	OperationDTO operationDTO = new OperationDTO();
	    	
			try {
				operationDTO = objectMapper
											.readValue(objectOfAList.toString(),
													OperationDTO.class);
				convertedList.add(operationDTO);
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
	
	public List<OperationDTO> getAll() {
		
		List<Operation> operations = operationRepo.findByOrderByNameAsc();
		List<OperationDTO> retValues = new ArrayList<OperationDTO>();
		for(Operation operation : operations) {
			OperationDTO op = new OperationDTO(operation);
			retValues.add(op);
		}
		return retValues;
	}
	
	public String formatJSON(String json) {
		json = json.replace("=", ":");
		json = json.replaceAll("__metadata:\\{[a-zA-Z0-9,':=\".()/_ -]*\\},", "");
		json = json.replace("/", "");
		json = json.replaceAll(":,", ":\"\",");
		json = json.replaceAll(":}", ":\"\"}");
		return json;
	}


	public void updateOperation(Operation oldOperation, OperationDTO newOperation) {
		oldOperation.setKind(newOperation.getKind());
		oldOperation.setName(newOperation.getName());
		OperationGroup operationGroup = operationGroupRepo.findByErpId(newOperation.getGroup()).get();
		oldOperation.setOperationGroup(operationGroup);
		switch(newOperation.getType()) {
		case "R":
			oldOperation.setType(OperationType.CROP_FARMING);
			break;
		case "G":
			oldOperation.setType(OperationType.VITICULTURE);
			break;
		case "V":
			oldOperation.setType(OperationType.FRUIT_GROWING);
			break;
		case "P":
			oldOperation.setType(OperationType.VEGETABLE);
			break;
		case "S":
			oldOperation.setType(OperationType.ANIMAL_HUSBANDRY);
			break;
		default:
			break;
		}
		operationRepo.save(oldOperation);
	}
	
	public void createOperation(OperationDTO newOperation) {
		Operation operation = new Operation(newOperation);
		OperationGroup operationGroup = operationGroupRepo.findByErpId(newOperation.getGroup()).get();
		operation.setOperationGroup(operationGroup);
		operationRepo.save(operation);
	}
	
	public void editOperation(OperationDTO operationDTO) {
		Operation operation = operationRepo.getOne(operationDTO.getId());
		switch(operationDTO.getType()) {
		case "RATARSTVO": 
			operation.setType(OperationType.CROP_FARMING);
			break;
		case "VINOGRADARSTVO":
			operation.setType(OperationType.VITICULTURE);
			break;
		case "VOĆARSTVO":
			operation.setType(OperationType.FRUIT_GROWING);
			break;
		case "POVRTARSTVO":
			operation.setType(OperationType.VEGETABLE);
			break;
		case "STOČARSTVO":
			operation.setType(OperationType.ANIMAL_HUSBANDRY);
			break;
		default:
			break;
		}
		OperationGroup operationGroup = operationGroupRepo.getOne(operationDTO.getOperationGroupId());
		operation.setOperationGroup(operationGroup);
		operationRepo.save(operation);
	}
	
	public List<OperationDTO> getAllByTypeAndGroup(String type, UUID groupId){
		List<OperationDTO> retValues = new ArrayList<OperationDTO>();
		switch(type) {
		case "RATARSTVO": 
			type = OperationType.CROP_FARMING.toString();
			break;
		case "VINOGRADARSTVO":
			type = OperationType.VITICULTURE.toString();
			break;
		case "VOĆARSTVO":
			type = OperationType.FRUIT_GROWING.toString();
			break;
		case "POVRTARSTVO":
			type = OperationType.VEGETABLE.toString();
			break;
		case "STOČARSTVO":
			type = OperationType.ANIMAL_HUSBANDRY.toString();
			break;
		default:
			break;
		}
		List<Operation> operations = operationRepo.findAllByTypeAndGroup(type, groupId);
		for(Operation operation : operations) {
			OperationDTO retValue = new OperationDTO(operation);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<OperationDTO> getAllByType(String type){
		List<OperationDTO> retValues = new ArrayList<OperationDTO>();
		switch(type) {
		case "RATARSTVO": 
			type = OperationType.CROP_FARMING.toString();
			break;
		case "VINOGRADARSTVO":
			type = OperationType.VITICULTURE.toString();
			break;
		case "VOĆARSTVO":
			type = OperationType.FRUIT_GROWING.toString();
			break;
		case "POVRTARSTVO":
			type = OperationType.VEGETABLE.toString();
			break;
		case "STOČARSTVO":
			type = OperationType.ANIMAL_HUSBANDRY.toString();
			break;
		default:
			break;
		}
		List<Operation> operations = operationRepo.findByType(type);
		for(Operation operation : operations) {
			OperationDTO retValue = new OperationDTO(operation);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<OperationDTO> getAllByGroup(UUID groupId){
		List<OperationDTO> retValues = new ArrayList<OperationDTO>();
		List<Operation> operations = operationRepo.findAllByGroup(groupId);
		for(Operation operation : operations) {
			OperationDTO retValue = new OperationDTO(operation);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<OperationDTO> getAllGroupByType(){
		List<Operation> operations = operationRepo.findAllByGroupByType();
		List<OperationDTO> retValues = new ArrayList<OperationDTO>();
		for(Operation operation : operations) {
			OperationDTO operationDTO = new OperationDTO(operation);
			retValues.add(operationDTO);
		}
		return retValues;
	}
	
	
}
