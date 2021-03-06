package org.mkgroup.zaga.workorderservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.OperationDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Entity
@Table(name = "operation", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class Operation {
	

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String kind;
	
	private boolean status;
	
	private String name;
	
	private Long erpId;
	
	@Enumerated(EnumType.STRING)
	private OperationType type;
	
	@ManyToOne
	private OperationGroup operationGroup;
	
	@OneToMany(mappedBy = "operation")
	private List<WorkOrderWorker> workOrders = new ArrayList<WorkOrderWorker>();
	
	public Operation(OperationDTO op) {
		this.kind = op.getKind();
		this.name = op.getName();
		this.erpId = op.getErpId();
		switch(op.getType()) {
		case "R":
			this.type = OperationType.CROP_FARMING;
			break;
		case "G":
			this.type = OperationType.VITICULTURE;
			break;
		case "V":
			this.type = OperationType.FRUIT_GROWING;
			break;
		case "P":
			this.type = OperationType.VEGETABLE;
			break;
		case "S":
			this.type = OperationType.ANIMAL_HUSBANDRY;
			break;
		default:
			break;
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getErpId() {
		return erpId;
	}

	public void setErpId(Long erpId) {
		this.erpId = erpId;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public OperationGroup getOperationGroup() {
		return operationGroup;
	}

	public void setOperationGroup(OperationGroup operationGroup) {
		this.operationGroup = operationGroup;
	}

	public List<WorkOrderWorker> getWorkOrders() {
		return workOrders;
	}

	public void setWorkOrders(List<WorkOrderWorker> workOrders) {
		this.workOrders = workOrders;
	}
	
}
