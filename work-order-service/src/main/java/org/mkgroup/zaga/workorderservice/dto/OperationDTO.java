package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class OperationDTO {

	private UUID id;
	
	private String kind;
	
}
