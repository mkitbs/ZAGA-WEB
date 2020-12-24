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
	private Double dayPeriod;
	private Double nightPeriod;
	private Double workPeriod;
	private Double initialState;
	private Double finalState;
	private Double sumState;
	private Double fuel;
	private int erpId = 0; //broj stavke WorkOrderEmployeeNumber
	private boolean deleted;
	
	public WorkOrderWorkerDTO(WorkOrderWorker wow) {
		this.id = wow.getId();
		this.user = new EmployeeDTO(wow.getUser());
		this.operation = new OperationDTO(wow.getOperation());
		this.nightPeriod = wow.getNightPeriod();
		this.dayPeriod = wow.getDayPeriod();
		this.workPeriod = wow.getWorkPeriod();
		this.deleted = wow.isDeleted();
		this.machine = new MachineDTO(wow.getMachine());
		if(wow.getConnectingMachine() != null) {
			this.connectingMachine = new MachineDTO(wow.getConnectingMachine());
		}else {
			this.connectingMachine = new MachineDTO();
			this.connectingMachine.setDbid("-1");
			this.connectingMachine.setName("BEZ PRIKLJUČNE MAŠINE");
		}
		this.initialState = wow.getInitialState();
		this.finalState = wow.getFinalState();
		this.sumState = wow.getSumState();
		this.fuel = wow.getFuel();
		this.erpId = wow.getErpId();
	}
}
