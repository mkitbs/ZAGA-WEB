package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.Worker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.json.JsonWriteFeature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value =  Include.NON_NULL)
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

}
