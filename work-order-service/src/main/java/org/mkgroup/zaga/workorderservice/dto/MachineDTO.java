package org.mkgroup.zaga.workorderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineDTO {
	
	@JsonProperty("Id")
	private String id; //iz sapa stize BEZ-MASINE

	@JsonProperty("CompanyCode")
	private String companyCode;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("OrgUnit")
	private String orgUnit;
	
	@JsonProperty("FuelType")
	private String fuelType;
	
	@JsonProperty("Type")
	private String machineType;
	
	@JsonProperty("OwnershipType")
	private String ownershipType;
	
	@JsonProperty("MachineGroupId")
	private Long machineGroup;
}
