package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
	private String userCreatedId;
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
	private boolean cancellation;
	private boolean noOperationOutput;
	private String numOfRefOrder;
	private String note;
	private String orgUnit;

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
		treated = Math.round(wo.getTreated() * 100.0 ) / 100.0;
		userCreatedId = wo.getUserCreatedSapId().toString();
		noOperationOutput = wo.isNoOperationOutput();
		numOfRefOrder = wo.getNumOfRefOrder();
		note = wo.getNote();
		orgUnit = wo.getOrgUnit();
		if (wo.getErpId() != null) {
			sapId = wo.getErpId();
		} else {
			sapId = 0;
		}

		this.materials = new ArrayList<SpentMaterialDTO>();
		for (SpentMaterial sm : wo.getMaterials()) {
			SpentMaterialDTO smDTO = new SpentMaterialDTO(sm);
			if (!smDTO.isDeleted()) {
				this.materials.add(smDTO);
			}

		}

		this.workers = new ArrayList<WorkOrderWorkerDTO>();
		for (WorkOrderWorker wow : wo.getWorkers()) {
			WorkOrderWorkerDTO wowDTO = new WorkOrderWorkerDTO(wow);
			if (!wowDTO.isDeleted()) {
				this.workers.add(wowDTO);
			}

		}

	}

	public WorkOrderDTO(WorkOrder wo, UUID idMaterial) {
		id = wo.getId();
		date = new DateDTO(wo);
		status = wo.getStatus().toString();
		operationName = wo.getOperation().getName();
		responsibleName = wo.getResponsible().getName();
		table = wo.getCrop().getField().getName();
		cropName = wo.getCrop().getName();
		treated = Math.round(wo.getTreated() * 100.0 ) / 100.0;
		if (wo.getErpId() != null) {
			sapId = wo.getErpId();
		} else {
			sapId = 0;
		}

		
		this.materials = new ArrayList<SpentMaterialDTO>();
		
		for (SpentMaterial sm : wo.getMaterials()) {
			if (sm.getMaterial().getId().equals(idMaterial)) {
				if (sm.isDeleted() != true) {
					SpentMaterialDTO smDTO = new SpentMaterialDTO(sm);
					this.materials.add(smDTO);
				}

			}
		}
		

	}

	public WorkOrderDTO(WorkOrder wo, Long idWorker) {
		id = wo.getId();
		date = new DateDTO(wo);
		status = wo.getStatus().toString();
		operationName = wo.getOperation().getName();
		responsibleName = wo.getResponsible().getName();
		table = wo.getCrop().getField().getName();
		cropName = wo.getCrop().getName();
		treated = Math.round(wo.getTreated() * 100.0 ) / 100.0;
		if (wo.getErpId() != null) {
			sapId = wo.getErpId();
		} else {
			sapId = 0;
		}

		this.workers = new ArrayList<WorkOrderWorkerDTO>();
		for (WorkOrderWorker wow : wo.getWorkers()) {
			if (wow.getUser().getPerNumber().equals(idWorker)) {
				if (wow.isDeleted() != true) {
					WorkOrderWorkerDTO wowDTO = new WorkOrderWorkerDTO(wow);
					this.workers.add(wowDTO);
				}

			}

		}

	}

	public WorkOrderDTO(WorkOrder wo, String machineName) {
		id = wo.getId();
		date = new DateDTO(wo);
		status = wo.getStatus().toString();
		operationName = wo.getOperation().getName();
		responsibleName = wo.getResponsible().getName();
		table = wo.getCrop().getField().getName();
		cropName = wo.getCrop().getName();
		treated = Math.round(wo.getTreated() * 100.0 ) / 100.0;
		if (wo.getErpId() != null) {
			sapId = wo.getErpId();
		} else {
			sapId = 0;
		}

		this.workers = new ArrayList<WorkOrderWorkerDTO>();
		for (WorkOrderWorker wow : wo.getWorkers()) {
			if (wow.getMachine().getName().equals(machineName)) {
				if (wow.isDeleted() != true) {
					WorkOrderWorkerDTO wowDTO = new WorkOrderWorkerDTO(wow);
					this.workers.add(wowDTO);
				}

			}

		}

	}

	public WorkOrderDTO(WorkOrder wo, String status2, String type) {
		id = wo.getId();
		date = new DateDTO(wo);
		status = status2;
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
		treated = Math.round(wo.getTreated() * 100.0 ) / 100.0;
		userCreatedId = wo.getUserCreatedSapId().toString();
		noOperationOutput = wo.isNoOperationOutput();
		numOfRefOrder = wo.getNumOfRefOrder();
		note = wo.getNote();
		orgUnit = wo.getOrgUnit();
		if (wo.getErpId() != null) {
			sapId = wo.getErpId();
		} else {
			sapId = 0;
		}

		this.materials = new ArrayList<SpentMaterialDTO>();
		for (SpentMaterial sm : wo.getMaterials()) {
			SpentMaterialDTO smDTO = new SpentMaterialDTO(sm);
			if (!smDTO.isDeleted()) {
				this.materials.add(smDTO);
			}

		}

		this.workers = new ArrayList<WorkOrderWorkerDTO>();
		for (WorkOrderWorker wow : wo.getWorkers()) {
			WorkOrderWorkerDTO wowDTO = new WorkOrderWorkerDTO(wow);
			if (!wowDTO.isDeleted()) {
				this.workers.add(wowDTO);
			}

		}

	}
	
	public WorkOrderDTO(WorkOrder wo, boolean type) {
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
		treated = Math.round(wo.getTreated() * 100.0 ) / 100.0;
		userCreatedId = wo.getUserCreatedSapId().toString();
		noOperationOutput = wo.isNoOperationOutput();
		numOfRefOrder = wo.getNumOfRefOrder();
		note = wo.getNote();
		orgUnit = wo.getOrgUnit();
		if (wo.getErpId() != null) {
			sapId = wo.getErpId();
		} else {
			sapId = 0;
		}

		

	}

}
