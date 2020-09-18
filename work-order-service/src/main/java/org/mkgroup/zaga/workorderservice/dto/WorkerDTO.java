package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDTO {
	
	private UUID id;
	private UUID userId;
	private UUID operationId;
	
	public WorkerDTO(Worker w) {
		id = w.getId();
		userId = w.getUserId();
	}
}
