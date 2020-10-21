package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.MachineGroup;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineGroupDTO {
	
	private UUID dbId;
	
	@JsonProperty("Id")
	private Long erpId;
	
	@JsonProperty("Name")
	private String name;
	
	public MachineGroupDTO(MachineGroup machineGroup) {
		dbId = machineGroup.getId();
		name = machineGroup.getName();
	}

}
