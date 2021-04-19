package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllWorkOrdersResponseDTO {

	private UUID id;
	private Long sapId;
	private String operation;
	private Date date;
	private String field;
	private double area;
	private String crop;
	private String responsiblePerson;
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
		this.operation = workOrder.getOperation().getName();
		this.date = workOrder.getDate();
		this.field = workOrder.getCrop().getField().getName();
		this.area = workOrder.getCrop().getArea();
		this.crop = workOrder.getCrop().getName();
		this.responsiblePerson = workOrder.getResponsible().getName();
		this.status = workOrder.getStatus().toString();
		this.orgUnit = workOrder.getOrgUnit();
		this.treated = workOrder.getTreated();
	}
}
