package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllWorkOrdersResponseDTO {

	private UUID id;
	private Long sapId;
	private String operationName;
	private String date;
	private String table;
	private double area;
	private String cropName;
	private String responsibleName;
	private String status;
	private String orgUnit;
	private double treated;
	
	public AllWorkOrdersResponseDTO(WorkOrder workOrder) {
		this.id = workOrder.getId();
		if(workOrder.getErpId() != null) {
			this.sapId = workOrder.getErpId();
		} else {
			this.sapId = 0L;
		}
		this.operationName = workOrder.getOperation().getName();
		String[] dateStr = workOrder.getDate().toString().split("-");
		this.date = dateStr[2].substring(0, 2) + "." + dateStr[1] + "." + dateStr[0] + ".";
		this.table = workOrder.getCrop().getField().getName();
		this.area = workOrder.getCrop().getArea();
		this.cropName = workOrder.getCrop().getName();
		this.responsibleName = workOrder.getResponsible().getName();
		if(workOrder.getStatus().equals(WorkOrderStatus.NEW)) {
			this.status = "Novi";
		} else if (workOrder.getStatus().equals(WorkOrderStatus.IN_PROGRESS)) {
			this.status = "U radu";
		} else if (workOrder.getStatus().equals(WorkOrderStatus.CLOSED)) {
			this.status = "Zatvoren";
		} else if (workOrder.getStatus().equals(WorkOrderStatus.CANCELLATION)) {
			this.status = "Storniran";
		}
		
		this.orgUnit = workOrder.getOrgUnit();
		this.treated = Math.round(workOrder.getTreated() * 100.0) / 100.0;
	}
}
