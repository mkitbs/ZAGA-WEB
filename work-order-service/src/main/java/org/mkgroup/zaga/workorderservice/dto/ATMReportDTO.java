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
public class ATMReportDTO {

	private UUID id;
	private Long sapId;
	private String operationName;
	private String date;
	private String table;
	private String cropName;
	private double area;
	private double treated;
	private String status;
	
	public ATMReportDTO(WorkOrder wo) {
		this.id = wo.getId();
		this.sapId = wo.getErpId();
		this.operationName = wo.getOperation().getName();
		String[] dateStr = wo.getDate().toString().split("-");
		this.date = dateStr[2].substring(0, 2) + "." + dateStr[1] + "." + dateStr[0] + ".";
		this.table = wo.getCrop().getField().getName();
		this.cropName = wo.getCrop().getName();
		this.area = wo.getCrop().getArea();
		this.treated = wo.getTreated();
		if(wo.getStatus().equals(WorkOrderStatus.NEW)) {
			this.status = "Novi";
		} else if(wo.getStatus().equals(WorkOrderStatus.IN_PROGRESS)) {
			this.status = "U radu";
		} else if(wo.getStatus().equals(WorkOrderStatus.CLOSED)) {
			this.status = "Zatvoren";
		} else if(wo.getStatus().equals(WorkOrderStatus.CANCELLATION)) {
			this.status = "Storniran";
		}
	}
	
}
