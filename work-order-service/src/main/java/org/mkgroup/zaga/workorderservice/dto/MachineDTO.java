package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineDTO {
	
	private UUID id;
	private String companyCode;
	private String orgUnit;
	private String fuelType;
	private String machineType;
	private String ownershipType;
	private Long machineGroup;
}
