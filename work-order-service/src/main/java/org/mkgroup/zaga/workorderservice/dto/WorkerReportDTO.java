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

	private EmployeeDTO worker;
	private List<WorkOrderDTO> workOrders = new ArrayList<WorkOrderDTO>();
	private double dayPeriodSum;
	private double nightPeriodSum;
	private double workPeriodSum;
}
