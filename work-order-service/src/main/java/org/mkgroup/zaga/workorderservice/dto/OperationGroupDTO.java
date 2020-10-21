package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.OperationGroup;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationGroupDTO {
	
	private UUID dbId;

	@JsonProperty("Id")
	Long id;
	
	@JsonProperty("Name")
	String name;
	
	public OperationGroupDTO(OperationGroup operationGroup) {
		dbId = operationGroup.getId();
		name = operationGroup.getName();
	}
}
