package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(value =  Include.NON_NULL)
public class OperationDTO {

	@JsonProperty("dbid")
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
	
	private UUID operationGroupId;
	
	public OperationDTO(Operation operation) {
		id = operation.getId();
		name = operation.getName();
		type = operation.getType().toString();
		operationGroupId = operation.getOperationGroup().getId();
		erpId = operation.getErpId();
		
	}
}
