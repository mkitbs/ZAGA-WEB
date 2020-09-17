package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineGroupDTO {
	
	private UUID id;
	
	@JsonProperty("Id")
	private Long erpId;
	
	@JsonProperty("Name")
	private String name;
}
