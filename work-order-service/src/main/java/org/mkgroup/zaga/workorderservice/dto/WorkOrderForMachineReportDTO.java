package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.UUID;

public class WorkOrderForMachineReportDTO {

	private UUID woId;
	private String sapId;
	private String atm;
	private Date workOrderDate;
	private String crop;
	private String field;
	private String worker;
	private String couplingMachine;
	private String machineState;
	private String fuel;
	private String woStatus;
	private String machineId;
	
	public WorkOrderForMachineReportDTO(UUID woId, String machineState) {
		this.woId = woId;
		this.machineState = machineState;
	}

	public UUID getWoId() {
		return woId;
	}

	public String getSapId() {
		return sapId;
	}

	public String getAtm() {
		return atm;
	}

	public Date getWorkOrderDate() {
		return workOrderDate;
	}

	public String getCrop() {
		return crop;
	}

	public String getField() {
		return field;
	}

	public String getWorker() {
		return worker;
	}

	public String getCouplingMachine() {
		return couplingMachine;
	}

	public String getMachineState() {
		return machineState;
	}

	public String getFuel() {
		return fuel;
	}

	public String getWoStatus() {
		return woStatus;
	}

	public String getMachineId() {
		return machineId;
	}
	
	
	
	
	
}
