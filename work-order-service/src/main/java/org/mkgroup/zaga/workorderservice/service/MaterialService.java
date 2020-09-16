package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.MaterialDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MaterialService {

	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	MaterialRepository materialRepo;
	
	public List<MaterialDTO> getMaterialsFromSAP() throws JSONException {
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
	    		authEncodingString.toString().getBytes());
		//Call SAP and retrieve cultureGroupSet
		ResponseEntity<Object> machineSet = 
				gwProxy.fetchMaterials("json", "Basic " + authHeader);
		String oDataString = machineSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		//Map to specific object
	    ArrayList<MaterialDTO> materialList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));

	    for(MaterialDTO mat : materialList) {
	    	Material m = new Material(mat);
	    	materialRepo.save(m);
	    }
		return materialList;
	}
	
	public ArrayList<MaterialDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<MaterialDTO> convertedList= new ArrayList<MaterialDTO>();
	    list.forEach(objectOfAList -> {
	    	MaterialDTO materialDto = new MaterialDTO();
	    	
			try {
				materialDto = objectMapper
											.readValue(objectOfAList.toString(),
													MaterialDTO.class);
				convertedList.add(materialDto);
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
	
	public Material getOne(UUID id) {
		try {
			Material material = materialRepo.getOne(id);
			return material;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
