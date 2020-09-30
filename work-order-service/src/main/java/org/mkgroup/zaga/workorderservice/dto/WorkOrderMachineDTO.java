package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrderMachine;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderMachineDTO {

	private UUID id;
	private EmployeeDTO user;
	private MachineDTO machine;
	private double initialState;
	private double finalState;
	private double sumState;
	private double workPeriod;
	private Date date;
	private double fuel;

	public WorkOrderMachineDTO(WorkOrderMachine wom){
		this.id = wom.getId();
		this.user = new EmployeeDTO(wom.getUser());
		this.machine = new MachineDTO(wom.getMachine());
		this.initialState = wom.getInitialState();
		this.finalState = wom.getFinalState();
		this.sumState = wom.getSumState();
		this.workPeriod = wom.getWorkPeriod();
		this.date = wom.getDate();
		this.fuel = wom.getFuel();
	}
}
