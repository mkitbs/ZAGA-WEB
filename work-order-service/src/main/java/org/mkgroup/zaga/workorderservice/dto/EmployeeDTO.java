package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.Worker;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
	
	private UUID id;
	
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
		id = user.getId();
		name = user.getName();
	}

	public EmployeeDTO(Worker w) {
		userId = w.getUserId();
		id = w.getId();
	}


}
