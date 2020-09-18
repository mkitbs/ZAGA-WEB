package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.VarietyDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.Culture;
import org.mkgroup.zaga.workorderservice.model.Variety;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.CultureRepository;
import org.mkgroup.zaga.workorderservice.repository.VarietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VarietyService {

	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	VarietyRepository varietyRepo;
	
	@Autowired
	CultureRepository cultureRepo;
	
	public List<VarietyDTO> getVarietiesFromSAP() throws JSONException {
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		//Call SAP and retrieve varietySet
		ResponseEntity<Object> varietySet = 
				gwProxy.fetchVarieties("json", "Basic " + authHeader);
		String oDataString = varietySet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		//Map to specific object
	    ArrayList<VarietyDTO> varietyList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));
	    
	    for(VarietyDTO variety : varietyList) {
	    	varietyRepo
	    			.findByErpId(variety.getId())
	    			.ifPresentOrElse(foundVariety -> updateVariety(foundVariety, variety),
	    							() -> createVariety(variety));
	    }

		return varietyList;
	}

	public ArrayList<VarietyDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<VarietyDTO> convertedList= new ArrayList<VarietyDTO>();
	    list.forEach(objectOfAList -> {
	    	VarietyDTO varietyDto = new VarietyDTO();
	    	
			try {
				varietyDto = objectMapper
											.readValue(objectOfAList.toString(),
													VarietyDTO.class);
				convertedList.add(varietyDto);
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
	
	private void createVariety(VarietyDTO variety) {
		Variety v = new Variety(variety);
		Culture culture = cultureRepo.findByErpId(variety.getCultureId()).get();
		v.setCulture(culture);
    	varietyRepo.save(v);
	}

	private void updateVariety(Variety oldVariety, VarietyDTO updatedVariety) {
		oldVariety.setFinishing(updatedVariety.getFinishing());
		oldVariety.setManufacturer(updatedVariety.getManufacturer());
		oldVariety.setName(updatedVariety.getName());
		oldVariety.setProtection(updatedVariety.getProtection());
		Culture culture = cultureRepo.findByErpId(updatedVariety.getCultureId()).get();
		oldVariety.setCulture(culture);
		varietyRepo.save(oldVariety);
	}
	
}
