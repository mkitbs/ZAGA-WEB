package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderMachine;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderDTO {
	
	private UUID id;
	private DateDTO date;
	private String status;
	private UUID cropId;
	private UUID operationId;
	private UUID responsibleId;
	private UUID tableId;
	private UUID cultureId;
	private List<SpentMaterialDTO> materials;
	private List<WorkOrderWorkerDTO> workers;
	private String operationName;
	private String responsibleName;
	private double area;
	private String table;
	private String cropName;
	private int year;
	private Date creationDate;
	private boolean closed;
	private double treated;
	private long sapId;
	
	public WorkOrderDTO(WorkOrder wo) {
		id = wo.getId();
		date = new DateDTO(wo);
		status = wo.getStatus().toString();
		cropId = wo.getCrop().getId();
		cultureId = wo.getCrop().getCulture().getId();
		operationId = wo.getOperation().getId();
		responsibleId = wo.getResponsible().getId();
		operationName = wo.getOperation().getName();
		responsibleName = wo.getResponsible().getName();
		area = wo.getCrop().getArea();
		table = wo.getCrop().getField().getName();
		tableId = wo.getCrop().getField().getId();
		cropName = wo.getCrop().getName();
		year = wo.getCrop().getYear();
		creationDate = wo.getCreationDate();
		closed = wo.isClosed();
		treated = wo.getTreated();
		sapId = wo.getErpId();
		
		this.materials = new ArrayList<SpentMaterialDTO>();
		for(SpentMaterial sm : wo.getMaterials()) {
			SpentMaterialDTO smDTO = new SpentMaterialDTO(sm);
			this.materials.add(smDTO);
		}
		
		
		this.workers = new ArrayList<WorkOrderWorkerDTO>();
		for(WorkOrderWorker wow : wo.getWorkers()) {
			WorkOrderWorkerDTO wowDTO = new WorkOrderWorkerDTO(wow);
			this.workers.add(wowDTO);
		}
		
	}	
}
