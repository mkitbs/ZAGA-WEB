package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;
import org.mkgroup.zaga.workorderservice.dto.UserElasticDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.feign.SearchServiceProxy;
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
	SearchServiceProxy ssProxy;
	
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
				userRepo
						.findByPerNumber(em.getPerNumber())
						.ifPresentOrElse(foundUser -> updateUser(foundUser, em), 
										() -> createUser(em));
		}
	    
	    List<UserElasticDTO> sendUsers = new ArrayList<UserElasticDTO>();
	    for(User u : userRepo.findByOrderByNameAsc()) {
	    	UserElasticDTO sendUser = new UserElasticDTO(u);
	    	sendUsers.add(sendUser);
	    }
	    ResponseEntity<?> response = ssProxy.sendEmployees(sendUsers);
	    System.out.println(response.getStatusCodeValue() + " STATUS");
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
	
	public void updateUser(User u, EmployeeDTO em) {
		u.setDepartment(em.getDepartment());
		u.setPosition(em.getPosition());
		u.setName(em.getName());
		userRepo.save(u);
	}
	
	public void createUser(EmployeeDTO employee) {
		User newUser = new User(employee);
		userRepo.save(newUser);
	}
	
	public void editUser(UserElasticDTO emp) {
		User user = userRepo.getOne(emp.getUserId());
		user.setDepartment(emp.getDepartment());
		user.setPosition(emp.getPosition());
		userRepo.save(user);
		UserElasticDTO u = new UserElasticDTO(user);
		ResponseEntity<?> response = ssProxy.editEmployee(u);
		System.out.println(response);
		//System.out.println(response.getStatusCodeValue() + " STATUS");
	}
}
