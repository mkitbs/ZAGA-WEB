package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Operation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OperationDTO {

	private UUID id;
	
	@JsonProperty("Id")
	private Long erpId;
	
	@JsonProperty("Kind")
	private String kind;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Group")
	private Long group;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Status")
	private String status;
	
	public OperationDTO(Operation operation) {
		id = operation.getId();
		name = operation.getName();
	}
}
