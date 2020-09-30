package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.UUID;

import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderWorkerDTO {

	private UUID id;
	private EmployeeDTO user;
	private OperationDTO operation;
	private Date date;
	private double dayPeriod;
	private double dayNightPeriod;
	private double workPeriod;
	
	public WorkOrderWorkerDTO(WorkOrderWorker wow) {
		this.id = wow.getId();
		this.user = new EmployeeDTO(wow.getUser());
		this.operation = new OperationDTO(wow.getOperation());
		this.date = wow.getDate();
		this.dayNightPeriod = wow.getDayNightPeriod();
		this.dayPeriod = wow.getDayWorkPeriod();
		this.workPeriod = wow.getWorkPeriod();
	}
}
