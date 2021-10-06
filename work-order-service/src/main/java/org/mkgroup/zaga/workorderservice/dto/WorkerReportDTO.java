package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerReportDTO {

	private String worker;
	private List<WorkOrderForEmployeeReportDTO> workOrders = new ArrayList<WorkOrderForEmployeeReportDTO>();
}
