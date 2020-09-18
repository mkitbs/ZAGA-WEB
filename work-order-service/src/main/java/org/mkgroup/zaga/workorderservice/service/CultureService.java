package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.CultureDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.Culture;
import org.mkgroup.zaga.workorderservice.model.CultureGroup;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.CultureGroupRepository;
import org.mkgroup.zaga.workorderservice.repository.CultureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CultureService {
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	CultureRepository cultureRepo;
	
	@Autowired
	CultureGroupRepository cultureGroupRepo;
	
	public List<CultureDTO> getCulturesFromSAP() throws JSONException {
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
				gwProxy.fetchCultures("json", "Basic " + authHeader);
		String oDataString = cultureGroupSet.getBody().toString().replace(":", "-");
		oDataString = oDataString.replace("=", ":");
		oDataString = oDataString.replace("/", "");
		//Map to specific object
	    ArrayList<CultureDTO> cultureList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));

	    for(CultureDTO c : cultureList) {
	    	cultureRepo
	    		.findByErpId(c.getErpId())
	    		.ifPresentOrElse(foundCulture -> updateCulture(foundCulture, c), 
	    						() -> createCulture(c));
	    }
	    
		return cultureList;
	}
	
	public ArrayList<CultureDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<CultureDTO> convertedList = new ArrayList<CultureDTO>();
	    list.forEach(objectOfAList -> {
	    	CultureDTO cultureDTO = new CultureDTO();
	    	
			try {
				cultureDTO = objectMapper
											.readValue(objectOfAList.toString(),
														CultureDTO.class);
				convertedList.add(cultureDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    });
	    return convertedList;
	}
	
	public Culture getOne(UUID id) {
		try {
			Culture culture = cultureRepo.getOne(id);
			return culture;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateCulture(Culture oldCulture, CultureDTO newCulture) {
		oldCulture.setName(newCulture.getName());
		CultureGroup cultureGroup = cultureGroupRepo.findByErpId(newCulture.getCultureGroupId()).get();
		oldCulture.setCultureGroup(cultureGroup);
		//dovrsiti
		cultureRepo.save(oldCulture);
	}
	
	public void createCulture(CultureDTO newCulture) {
		Culture culture = new Culture(newCulture);
		CultureGroup cultureGroup = cultureGroupRepo.findByErpId(newCulture.getCultureGroupId()).get();
		culture.setCultureGroup(cultureGroup);
		cultureRepo.save(culture);
	}
}
