package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderMachine;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderMachineDTO {

private Long id;
	
	private EmployeeDTO user;
	private MachineDTO machine;
	private double initialState;
	private double finalState;
	private double sumState;
	private double workPeriod;
	private Date date;

	public WorkOrderMachineDTO(WorkOrderMachine wom){
		this.id = wom.getId();
		this.user = new EmployeeDTO(wom.getUser());
		this.machine = new MachineDTO(wom.getMachine());
		this.initialState = wom.getInitialState();
		this.finalState = wom.getFinalState();
		this.sumState = wom.getSumState();
		this.workPeriod = wom.getWorkPeriod();
		this.date = wom.getDate();
	}
}
