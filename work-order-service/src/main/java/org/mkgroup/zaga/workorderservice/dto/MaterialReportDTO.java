package org.mkgroup.zaga.workorderservice.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReportDTO {
	
	private UUID materialId;
	private List<WorkOrderDTO> workOrders;
}
