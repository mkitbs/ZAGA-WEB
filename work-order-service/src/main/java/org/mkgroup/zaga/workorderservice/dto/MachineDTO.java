package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Machine;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineDTO {
	
	private UUID id;
	
	@JsonProperty("Id")
	private String erpId; //iz sapa stize BEZ-MASINE

	@JsonProperty("CompanyCode")
	private String companyCode;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("OrgUnit")
	private String orgUnit;
	
	@JsonProperty("FuelType")
	private String fuelType;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("OwnershipType")
	private String ownershipType;
	
	@JsonProperty("MachineGroupId")
	private Long machineGroup;
	
	public MachineDTO(Machine machine) {
		id = machine.getId();
		name = machine.getName();
	}

}
