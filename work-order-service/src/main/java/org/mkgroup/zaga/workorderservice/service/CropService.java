package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.AreasByCropsDTO;
import org.mkgroup.zaga.workorderservice.dto.CropDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.model.Culture;
import org.mkgroup.zaga.workorderservice.model.Field;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.CropRepository;
import org.mkgroup.zaga.workorderservice.repository.CultureRepository;
import org.mkgroup.zaga.workorderservice.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CropService {

	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	CropRepository cropRepo;
	
	@Autowired
	FieldRepository fieldRepo;
	
	@Autowired
	CultureRepository cultureRepo;
	
	public List<CropDTO> getCropsFromSAP() throws JSONException {
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		//Call SAP and retrieve cultureGroupSet
		ResponseEntity<Object> cropSet = 
				gwProxy.fetchCrops("json", "Basic " + authHeader);
		String oDataString = cropSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		
		//Map to specific object
	    ArrayList<CropDTO> cropList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));
	    for(CropDTO crop : cropList) {
	    	cropRepo
	    		.findByErpId(crop.getErpId())
	    		.ifPresentOrElse(foundCrop -> updateCrop(foundCrop, crop), 
	    						() -> createCrop(crop));
	    }
		return cropList;
	}
	
	public ArrayList<CropDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<CropDTO> convertedList= new ArrayList<CropDTO>();
	    list.forEach(objectOfAList -> {
	    	CropDTO cropDTO = new CropDTO();
	    	
			try {
				cropDTO = objectMapper
											.readValue(objectOfAList.toString(),
													CropDTO.class);
				convertedList.add(cropDTO);
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
	
	public Crop getOne(UUID id) {
		try {
			Crop crop = cropRepo.getOne(id);
			return crop;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void createCrop(CropDTO newCrop) {
		Crop crop = new Crop(newCrop);
		Field field = fieldRepo.findByErpId(newCrop.getErpFieldId()).get();
		Culture culture = cultureRepo.findByErpId(newCrop.getErpCultureId()).get();
		crop.setField(field);
		crop.setCulture(culture);
		cropRepo.save(crop);
	}
	
	public void updateCrop(Crop oldCrop, CropDTO updatedCrop) {
		oldCrop.setArea(updatedCrop.getArea());
		oldCrop.setCompanyCode(updatedCrop.getCompanyCode());
		oldCrop.setName(updatedCrop.getName());
		oldCrop.setYear(updatedCrop.getYear());
		oldCrop.setOrgUnit(updatedCrop.getOrganisationUnit());
		Field field = fieldRepo.findByErpId(updatedCrop.getErpFieldId()).get();
		oldCrop.setField(field);
		Culture culture = cultureRepo.findByErpId(updatedCrop.getErpCultureId()).get();
		oldCrop.setCulture(culture);
		cropRepo.save(oldCrop);
	}
	
	public List<CropDTO> getAllByFieldAndYear(UUID fieldId, int year){
		try {
			List<Crop> crops = cropRepo.findByFieldAndYear(fieldId, year);
			List<CropDTO> cropsDTO = new ArrayList<CropDTO>();
			for(Crop crop : crops) {
				CropDTO cropDTO = new CropDTO(crop);
				cropsDTO.add(cropDTO);
			}
			return cropsDTO;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CropDTO> getAllByFieldAndCulture(UUID fieldId, UUID cultureId){
		try {
			List<Crop> crops = cropRepo.findByFieldAndCulture(fieldId, cultureId);
			List<CropDTO> cropsDTO = new ArrayList<CropDTO>();
			for(Crop crop : crops) {
				CropDTO cropDTO = new CropDTO(crop);
				cropsDTO.add(cropDTO);
			}
			return cropsDTO;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CropDTO> getAllByField(UUID fieldId){
		try {
			List<Crop> crops = cropRepo.findByField(fieldId);
			List<CropDTO> cropsDTO = new ArrayList<CropDTO>();
			for(Crop crop : crops) {
				CropDTO cropDTO = new CropDTO(crop);
				cropsDTO.add(cropDTO);
			}
			return cropsDTO;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CropDTO> getAllByCulture(UUID cultureId){
		try {
			List<Crop> crops = cropRepo.findByCulture(cultureId);
			List<CropDTO> cropsDTO = new ArrayList<CropDTO>();
			for(Crop crop : crops) {
				CropDTO cropDTO = new CropDTO(crop);
				cropsDTO.add(cropDTO);
			}
			return cropsDTO;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void editCrop(CropDTO cropDTO) {
		Crop crop = cropRepo.getOne(cropDTO.getId());
		crop.setArea(cropDTO.getArea());
		crop.setYear(cropDTO.getYear());
		Field field = fieldRepo.getOne(cropDTO.getFieldId());
		crop.setField(field);
		Culture culture = cultureRepo.getOne(cropDTO.getCultureId());
		crop.setCulture(culture);
		cropRepo.save(crop);
	}
	
	public List<AreasByCropsDTO> getAreasByCrops() {
		List<AreasByCropsDTO> retVals = cropRepo.findAreasByCrops();
		return retVals;
	}
	
}
