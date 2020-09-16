package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Material;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialDTO {

	private UUID id;
	
	@JsonProperty("Id")
	Long erpId;
	
	@JsonProperty("Name")
	String name;
	
	@JsonProperty("Unit")
	String unit;
	
	
	@JsonProperty("Group")
	String group;
	
	public MaterialDTO(Material material) {
		id = material.getId();
		name = material.getName();
	}

}
