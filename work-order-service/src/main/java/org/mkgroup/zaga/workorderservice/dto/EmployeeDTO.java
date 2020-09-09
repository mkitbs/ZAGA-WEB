package org.mkgroup.zaga.workorderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
	
	@JsonProperty("Id")
	Long id;
	
	@JsonProperty("Name")
	String name;
	
	@JsonProperty("Department")
	String department;
	
	@JsonProperty("Position")
	String position;

}
