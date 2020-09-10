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
	
	private Date start;
	private Date end;
	private String status;
	private byte[] cropId;
	private byte[] operationId;
	private List<MaterialDTO> materials;
	private List<MachineDTO> machines;
	private List<EmployeeDTO> employees;
}
