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
	private String sapId;
	private String atm;
	private Date workOrderDate;
	private String field;
	private String crop;
	private double area;
	private double treated;
	private String status;
	
	public ATMReportDTO(WorkOrder wo) {
		this.id = wo.getId();
		if(wo.getErpId() != null) {
			this.sapId = wo.getErpId().toString();
		}
		this.atm = wo.getOperation().getName();
		this.workOrderDate = wo.getDate();
		this.field = wo.getCrop().getField().getName();
		this.crop = wo.getCrop().getName();
		this.area = wo.getCrop().getArea();
		this.treated = Math.round(wo.getTreated() * 100.0) / 100.0;
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
