package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkerTimeTracking;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTimeTrackingDTO {

	private UUID dbId;
	private Long erpId;
	
	public ResponseTimeTrackingDTO(WorkerTimeTracking wtt) {
		this.dbId = wtt.getId();
		this.erpId = wtt.getErpId();
	}
}
