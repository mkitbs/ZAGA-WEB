package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialDTO {
	
	@JsonProperty("Id")
	UUID id;
	
	@JsonProperty("Name")
	String name;
	
	@JsonProperty("Unit")
	String unit;
	
	
	@JsonProperty("Group")
	String group;
	
}
