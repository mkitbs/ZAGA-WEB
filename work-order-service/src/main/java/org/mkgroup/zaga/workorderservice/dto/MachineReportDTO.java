package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineReportDTO {

	private MachineDTO machine;
	private List<WorkOrderDTO> workOrders = new ArrayList<WorkOrderDTO>();
	private double finalStateSum;
	private double fuelSum;
}
