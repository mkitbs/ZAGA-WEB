package org.mkgroup.zaga.workorderservice.dto;


import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderWorkerDTO {

	private UUID id;
	private EmployeeDTO user;
	private OperationDTO operation;
	private MachineDTO machine;
	private MachineDTO connectingMachine;
	private double dayPeriod;
	private double nightPeriod;
	private double workPeriod;
	private double initialState;
	private double finalState;
	private double sumState;
	private double fuel;
	
	public WorkOrderWorkerDTO(WorkOrderWorker wow) {
		this.id = wow.getId();
		this.user = new EmployeeDTO(wow.getUser());
		this.operation = new OperationDTO(wow.getOperation());
		this.nightPeriod = wow.getNightPeriod();
		this.dayPeriod = wow.getDayPeriod();
		this.workPeriod = wow.getWorkPeriod();
		this.machine = new MachineDTO(wow.getMachine());
		this.connectingMachine = new MachineDTO(wow.getConnectingMachine());
		this.initialState = wow.getInitialState();
		this.finalState = wow.getFinalState();
		this.sumState = wow.getSumState();
		this.fuel = wow.getFuel();
	}
}
