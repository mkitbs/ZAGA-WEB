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
import org.mkgroup.zaga.workorderservice.model.CultureType;
import org.mkgroup.zaga.workorderservice.model.OrgCon;
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
		oDataString = formatJSON(oDataString);
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
		switch(newCulture.getType()) {
		case "V":
			oldCulture.setType(CultureType.FRUIT);
			break;
		case "P":
			oldCulture.setType(CultureType.VEGETABLE);
			break;
		case "R":
			oldCulture.setType(CultureType.CROP_FARMING);
			break;
		case "G": 
			oldCulture.setType(CultureType.VITICULTURE);
			break;
		default: 
			break;
		}
		cultureRepo.save(oldCulture);
	}
	
	public void createCulture(CultureDTO newCulture) {
		Culture culture = new Culture(newCulture);
		CultureGroup cultureGroup = cultureGroupRepo.findByErpId(newCulture.getCultureGroupId()).get();
		culture.setCultureGroup(cultureGroup);
		cultureRepo.save(culture);
	}
	
	public void editCulture(CultureDTO cultureDTO) {
		Culture culture = cultureRepo.getOne(cultureDTO.getDbId());
		switch(cultureDTO.getOrgCon()) {
		case "ORGANSKA":
			culture.setOrgCon(OrgCon.ORGANIC);
			break;
		case "KONVENCIONALNA":
			culture.setOrgCon(OrgCon.CONVENTIONAL);
			break;
		default:
			break;
		}
		switch(cultureDTO.getType()) {
		case "VOĆE":
			culture.setType(CultureType.FRUIT);
			break;
		case "POVRĆE":
			culture.setType(CultureType.VEGETABLE);
			break;
		case "RATARSKA":
			culture.setType(CultureType.CROP_FARMING);
			break;
		case "VINOGRADARSKA":
			culture.setType(CultureType.VITICULTURE);
			break;
		default:
			break;
		}
		CultureGroup cultureGroup = cultureGroupRepo.getOne(cultureDTO.getCultureGroup());
		culture.setCultureGroup(cultureGroup);
		cultureRepo.save(culture);
	}
	
	public List<CultureDTO> getAllByOrgCon(String orgCon){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllByOrgCon(orgCon);
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<CultureDTO> getAllByCultureType(String type){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllByType(type);
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<CultureDTO> getAllByCultureGroup(UUID id){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllByCultureGroup(id);
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<CultureDTO> getAllByOrgConCultureTypeCultureGroup(String orgCon, String type, UUID id){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllByOrgConCultureTypeCultureGroup(orgCon, type, id);
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<CultureDTO> getAllByOrgConAndCultureType(String orgCon, String type){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllByOrgConAndCultureType(orgCon, type);
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<CultureDTO> getAllByOrgConAndCultureGroup(String orgCon, UUID id){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllByOrgConAndCultureGroup(orgCon, id);
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<CultureDTO> getAllByCultureTypeAndCultureGroup(String type, UUID id){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllByCultureTypeAndCultureGroup(type, id);
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<CultureDTO> getAllGroupByCultureType(){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllGroupByCultureType();
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
		}
		return retValues;
	}
	
	public List<CultureDTO> getAllGroupByProductionType(){
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		List<Culture> cultures = cultureRepo.findAllGroupByProductionType();
		for(Culture culture : cultures) {
			CultureDTO retValue = new CultureDTO(culture);
			retValues.add(retValue);
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
}
