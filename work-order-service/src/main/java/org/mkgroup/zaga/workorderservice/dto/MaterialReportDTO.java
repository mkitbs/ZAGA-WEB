package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReportDTO {
	
	private SpentMaterialDTO material;
	private List<WorkOrderDTO> workOrders = new ArrayList<WorkOrderDTO>();
	private MaterialReportSumDTO sumQuantity;
	private MaterialReportSumDTO sumSpent;
	private MaterialReportSumDTO sumAllQuantityPerUnit;
	private MaterialReportSumDTO sumAllSpentPerUnit;
}
