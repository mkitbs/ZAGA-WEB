package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkerTimeTracking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTrackingDTO {

	private Date startTime;
	private Date endTime;
	private String type;
	private UUID wowId;
	
	public TimeTrackingDTO(WorkerTimeTracking time) {
		this.startTime = time.getStartTime();
		this.endTime = time.getEndTime();
		this.type = time.getType().toString();
		this.wowId = time.getWorkOrderWorker().getId();
	}
}
