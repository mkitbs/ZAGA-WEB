package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Material;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value =  Include.NON_NULL)
public class MaterialDTO {

	private UUID dbid;
	
	@JsonProperty("Id")
	Long erpId;
	
	@JsonProperty("Name")
	String name;
	
	@JsonProperty("Unit")
	String unit;
	
	
	@JsonProperty("Group")
	String group;
	
	public MaterialDTO(Material material) {
		dbid = material.getId();
		name = material.getName();
		unit = material.getUnit();
		erpId = material.getErpId();
	}

}
