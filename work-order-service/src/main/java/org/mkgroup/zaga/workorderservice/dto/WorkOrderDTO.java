package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderDTO {
	
	private UUID id;
	private Date start;
	private Date end;
	private String status;
	private UUID cropId;
	private UUID operationId;
	private List<MaterialDTO> materials;
	private List<MachineDTO> machines;
	private List<EmployeeDTO> employees;
	
}
