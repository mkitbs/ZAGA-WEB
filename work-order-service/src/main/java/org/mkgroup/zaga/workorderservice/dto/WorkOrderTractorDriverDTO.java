package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderTractorDriverDTO {

	private long workOrderSapId;
	private Date workOrderDate;
	private String worker;
	private String field;
	private String crop;
	private String operation;
	private UUID wowId;
	private String wowStatus;
	private boolean inProgress;
	
	public WorkOrderTractorDriverDTO(WorkOrderWorker wow, boolean inProgress) {
		this.workOrderSapId = wow.getWorkOrder().getErpId();
		this.workOrderDate = wow.getWorkOrder().getDate();
		this.worker = wow.getUser().getName();
		this.field = wow.getWorkOrder().getCrop().getField().getName();
		this.crop = wow.getWorkOrder().getCrop().getName() + " " + wow.getWorkOrder().getCrop().getYear();
		this.operation = wow.getOperation().getName();
		this.wowId = wow.getId();
		this.wowStatus = wow.getStatus().toString();
		this.inProgress = inProgress;
	}
}
