package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value =  Include.NON_NULL)
public class EmployeeDTO {
	
	private String id;
	
	private UUID userId;
	
	@JsonProperty("Name")
	String name;
	
	@JsonProperty("Department")
	String department;
	
	@JsonProperty("Position")
	String position;

	@JsonProperty("Id")
	Long perNumber;
	
	public EmployeeDTO(User user) {
		userId = user.getId();
		name = user.getName();
		perNumber = user.getPerNumber();
		department = user.getDepartment();
		position = user.getPosition();
	}

}
