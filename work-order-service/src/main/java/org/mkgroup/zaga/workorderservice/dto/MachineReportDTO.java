package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineReportDTO {

	private String machine;
	//private List<WorkOrderForMachineReportDTO> workOrders = new ArrayList<WorkOrderForMachineReportDTO>();
	private List<String> workOrders = new ArrayList<String>();
	//private List<WorkOrderDTO> workOrders = new ArrayList<WorkOrderDTO>();
}
