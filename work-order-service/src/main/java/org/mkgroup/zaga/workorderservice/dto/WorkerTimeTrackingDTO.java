package org.mkgroup.zaga.workorderservice.dto;

import java.util.List;

import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.model.WorkerTimeTracking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerTimeTrackingDTO {

	private WorkOrderTractorDriverDTO headerInfo;
	private List<TimeTrackingDTO> times;
	
}
