package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Machine;

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
public class MachineDTO {
	
	private String dbid;
	
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
	private Long machineGroupId;
	
	private UUID machineGroup;
	
	public MachineDTO(Machine machine) {
		dbid = machine.getId().toString();
		name = machine.getName();
		type = machine.getType().toString();
		fuelType = machine.getFuelType().toString();
		machineGroup = machine.getMachineGroupId().getId();
		erpId = machine.getErpId();
	}

}
