package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class WorkOrderWorker {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name="worker_id", nullable=true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="operation_id", nullable=true)
	private Operation operation;
	
	@ManyToOne
	@JoinColumn(name="work_order_id", nullable=true)
	private WorkOrder workOrder;
	
	@ManyToOne
	@JoinColumn(name="machine_id", nullable=true)
	private Machine machine;
	
	@ManyToOne
	@JoinColumn(name="connecting_machine_id", nullable = true)
	private Machine connectingMachine;
	
	@OneToMany(mappedBy = "workOrderWorker")
	private List<WorkerTimeTracking> workersTimeTracking;
	
	private Double dayPeriod = -1.0;
	
	private Double nightPeriod = -1.0;
	
	private Double workPeriod = -1.0;
	
	private Double initialState = -1.0;
	
	private Double finalState = -1.0;
	
	private Double sumState = -1.0;
	
	private Double fuel = -1.0;
	
	private int erpId = 0; //broj stavke WorkOrderEmployeeNumber
	
	private boolean deleted = false; //logicko brisanje zbog SAP-a
	
	private Double operationOutput = -1.0;
	
	@Enumerated(EnumType.STRING)
	private WorkOrderWorkerStatus status;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public WorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public Machine getConnectingMachine() {
		return connectingMachine;
	}

	public void setConnectingMachine(Machine connectingMachine) {
		this.connectingMachine = connectingMachine;
	}

	public List<WorkerTimeTracking> getWorkersTimeTracking() {
		return workersTimeTracking;
	}

	public void setWorkersTimeTracking(List<WorkerTimeTracking> workersTimeTracking) {
		this.workersTimeTracking = workersTimeTracking;
	}

	public Double getDayPeriod() {
		return dayPeriod;
	}

	public void setDayPeriod(Double dayPeriod) {
		this.dayPeriod = dayPeriod;
	}

	public Double getNightPeriod() {
		return nightPeriod;
	}

	public void setNightPeriod(Double nightPeriod) {
		this.nightPeriod = nightPeriod;
	}

	public Double getWorkPeriod() {
		return workPeriod;
	}

	public void setWorkPeriod(Double workPeriod) {
		this.workPeriod = workPeriod;
	}

	public Double getInitialState() {
		return initialState;
	}

	public void setInitialState(Double initialState) {
		this.initialState = initialState;
	}

	public Double getFinalState() {
		return finalState;
	}

	public void setFinalState(Double finalState) {
		this.finalState = finalState;
	}

	public Double getSumState() {
		return sumState;
	}

	public void setSumState(Double sumState) {
		this.sumState = sumState;
	}

	public Double getFuel() {
		return fuel;
	}

	public void setFuel(Double fuel) {
		this.fuel = fuel;
	}

	public int getErpId() {
		return erpId;
	}

	public void setErpId(int erpId) {
		this.erpId = erpId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public WorkOrderWorkerStatus getStatus() {
		return status;
	}

	public void setStatus(WorkOrderWorkerStatus status) {
		this.status = status;
	}

	public Double getOperationOutput() {
		return operationOutput;
	}

	public void setOperationOutput(Double operationOutput) {
		this.operationOutput = operationOutput;
	}
	
}
