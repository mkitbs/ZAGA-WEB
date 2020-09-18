package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.FieldGroupDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.FieldGroup;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.FieldGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FieldGroupService {
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	FieldGroupRepository fieldGroupRepo;
	
	public List<FieldGroupDTO> getFieldGroupsFromSAP() throws JSONException{
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
			    authEncodingString.toString().getBytes());
		//Call SAP and retrieve fieldGroupSet
		ResponseEntity<Object> fieldGroupSet = 
				gwProxy.fetchFieldGroups("json", "Basic " + authHeader);
		String oDataString = fieldGroupSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		//Map to specific object
		ArrayList<FieldGroupDTO> fieldGroupList = 
								convertObjectToLocalList(odataConvertor
															.convertODataSetToDTO
																	(oDataString));
		for(FieldGroupDTO field : fieldGroupList) {
			fieldGroupRepo
				.findByErpId(field.getId())
				.ifPresentOrElse(foundFieldGroup -> updateFiledGroup(foundFieldGroup, field),
								() -> createFieldGroup(field));
		}
		
		return fieldGroupList;
	}

	private ArrayList<FieldGroupDTO> convertObjectToLocalList(Object listAsObject) {
		List<?> list = (List<?>) listAsObject;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		ArrayList<FieldGroupDTO> convertedList = new ArrayList<FieldGroupDTO>();
		list.forEach(objectOfAList -> {
			FieldGroupDTO fieldDTO = new FieldGroupDTO();
			
			try {
				fieldDTO = objectMapper
										
											.readValue(objectOfAList.toString(), 
														FieldGroupDTO.class);
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
	
	private void createFieldGroup(FieldGroupDTO field) {
		FieldGroup fieldGroup = new FieldGroup(field);
		fieldGroupRepo.save(fieldGroup);
	}

	private void updateFiledGroup(FieldGroup oldFieldGroup, FieldGroupDTO updatedFieldGroup) {
		oldFieldGroup.setCompanyCode(updatedFieldGroup.getCompanyCode());
		oldFieldGroup.setName(updatedFieldGroup.getName());
		oldFieldGroup.setOrgUnit(updatedFieldGroup.getOrgUnit());
		fieldGroupRepo.save(oldFieldGroup);
	}
}
