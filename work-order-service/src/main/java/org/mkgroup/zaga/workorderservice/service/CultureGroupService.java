package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.CultureGroupDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.CultureGroup;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.CultureGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CultureGroupService {
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	CultureGroupRepository cultureGroupRepo;
	
	public List<CultureGroupDTO> getCultureGroupsFromSAP() throws JSONException {
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
				gwProxy.fetchCultureGroups("json", "Basic " + authHeader);
		String oDataString = cultureGroupSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		//Map to specific object
	    ArrayList<CultureGroupDTO> cultureGroupList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));
	    for(CultureGroupDTO culture : cultureGroupList) {
	    	cultureGroupRepo
	    			.findByErpId(culture.getId())
	    			.ifPresentOrElse(foundCultureGroup -> updateCultureGroup(foundCultureGroup, culture),
	    							() -> createCultureGroup(culture));
	    			
	    }

		return cultureGroupList;
	}

	public ArrayList<CultureGroupDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<CultureGroupDTO> convertedList= new ArrayList<CultureGroupDTO>();
	    list.forEach(objectOfAList -> {
	    	CultureGroupDTO cultureDTO = new CultureGroupDTO();
	    	
			try {
				cultureDTO = objectMapper
											.readValue(objectOfAList.toString(),
														CultureGroupDTO.class);
				convertedList.add(cultureDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    });
	    return convertedList;
	}
	
	public String formatJSON(String json) {
		System.out.println(json);
		json = json.replace("=", ":");
		json = json.replaceAll("__metadata:\\{[a-zA-Z0-9,':=\".()/_ -]*\\},", "");
		json = json.replace("/", "");
		json = json.replaceAll(":,", ":\"\",");
		json = json.replaceAll(":}", ":\"\"}");
		System.out.println(json);
		return json;
	}
	
	private void createCultureGroup(CultureGroupDTO culture) {
		CultureGroup c = new CultureGroup(culture);
    	cultureGroupRepo.save(c);
	}

	private void updateCultureGroup(CultureGroup oldCultureGroup, CultureGroupDTO updatedCultureGroup) {
		oldCultureGroup.setName(updatedCultureGroup.getName());
		cultureGroupRepo.save(oldCultureGroup);
	}
	
	public List<CultureGroupDTO> getAll(){
		try {
			List<CultureGroup> cultureGroups = cultureGroupRepo.findAll();
			List<CultureGroupDTO> retValues = new ArrayList<CultureGroupDTO>();
			for(CultureGroup cultureGroup : cultureGroups) {
				CultureGroupDTO retValue = new CultureGroupDTO(cultureGroup);
				retValues.add(retValue);
			}
			return retValues;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
