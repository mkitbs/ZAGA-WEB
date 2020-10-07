package org.mkgroup.zaga.workorderservice.dtoSAP;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderEmployeeSAP {

	private String WorkOrderEmployeeNumber;
	private String EmployeeId;
	private String OperationId;
	private String WorkEffectiveHours;
	private String WorkNightHours;
	private String WorkSundayHours;
	private String WorkHolidayHours;
	private String OvertimeWork;
	private String OperationOutput;
	private String OperationOutputUnit;
	private String NoOperationOutput;
	private String MasterMachineId;
	private String SlaveMachineId;
	private String MachineTimeStart;
	private String MachineTimeEnd;
	private String MachineTime;
	private String MachineEffectiveHours;
	private String MachineAreaOutput;
	private String SpentFuel;
	private String Deleted;
}
