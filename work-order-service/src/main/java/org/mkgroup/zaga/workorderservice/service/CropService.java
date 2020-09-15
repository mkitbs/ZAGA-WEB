package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.CropDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.CropRepository;
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
}
