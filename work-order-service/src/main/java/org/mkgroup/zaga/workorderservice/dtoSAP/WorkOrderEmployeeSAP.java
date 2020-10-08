package org.mkgroup.zaga.workorderservice.dtoSAP;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderEmployeeSAP {

	@JsonProperty("WorkOrderEmployeeNumber")
	private String WorkOrderEmployeeNumber;
	@JsonProperty("EmployeeId")
	private String EmployeeId;
	@JsonProperty("OperationId")
	private String OperationId;
	@JsonProperty("WorkEffectiveHours")
	private String WorkEffectiveHours;
	@JsonProperty("WorkNightHours")
	private String WorkNightHours;
	@JsonProperty("WorkSundayHours")
	private String WorkSundayHours;
	@JsonProperty("WorkHolidayHours")
	private String WorkHolidayHours;
	@JsonProperty("OvertimeWork")
	private String OvertimeWork;
	@JsonProperty("OperationOutput")
	private String OperationOutput;
	@JsonProperty("OperationOutputUnit")
	private String OperationOutputUnit;
	@JsonProperty("NoOperationOutput")
	private String NoOperationOutput;
	@JsonProperty("MasterMachineId")
	private String MasterMachineId;
	@JsonProperty("SlaveMachineId")
	private String SlaveMachineId;
	@JsonProperty("MachineTimeStart")
	private String MachineTimeStart;
	@JsonProperty("MachineTimeEnd")
	private String MachineTimeEnd;
	@JsonProperty("MachineTime")
	private String MachineTime;
	@JsonProperty("MachineEffectiveHours")
	private String MachineEffectiveHours;
	@JsonProperty("MachineAreaOutput")
	private String MachineAreaOutput;
	@JsonProperty("SpentFuel")
	private String SpentFuel;
	@JsonProperty("Deleted")
	private String Deleted;
}
