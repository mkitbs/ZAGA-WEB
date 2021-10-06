package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReportDTO {
	
	private SpentMaterialDTO material;
	private List<WorkOrderForMaterialReportDTO> workOrders = new ArrayList<WorkOrderForMaterialReportDTO>();
	
}
