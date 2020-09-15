package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeService {

	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	UserRepository userRepo;
	
	public List<EmployeeDTO> getEmployeesFromSAP() throws JSONException {
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
				gwProxy.fetchEmployees("json", "Basic " + authHeader);
		String oDataString = cultureGroupSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		//Map to specific object
	    ArrayList<EmployeeDTO> employeeList = 
	    						convertObjectToLocalList(odataConvertor
														.convertODataSetToDTO
																(oDataString));

	    for(EmployeeDTO em:employeeList) {
			User u = new User(em);
			userRepo.save(u);
		}
		return employeeList;
	}
	
	public ArrayList<EmployeeDTO> convertObjectToLocalList(Object listAsObject) {
	    List<?> list = (List<?>) listAsObject;
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    ArrayList<EmployeeDTO> convertedList= new ArrayList<EmployeeDTO>();
	    list.forEach(objectOfAList -> {
	    	EmployeeDTO emplDto = new EmployeeDTO();
	    	
			try {
				emplDto = objectMapper
											.readValue(objectOfAList.toString(),
														EmployeeDTO.class);
				convertedList.add(emplDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    });
	    return convertedList;
	}
	
	public User getOne(UUID id) {
		try {
			User user = userRepo.getOne(id);
			return user;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
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
