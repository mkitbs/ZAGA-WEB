package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.Worker;

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
	private UUID responsibleId;
	private List<MaterialDTO> materials;
	private List<MachineDTO> machines;
	private List<EmployeeDTO> workers;
	private String operationName;
	private String responsibleName;
	private double area;
	private String table;
	private String cropName;
	private int year;
	
	public WorkOrderDTO(WorkOrder wo) {
		id = wo.getId();
		start = wo.getStartDate();
		end = wo.getEndDate();
		status = wo.getStatus().toString();
		cropId = wo.getCrop().getId();
		operationId = wo.getOperation().getId();
		responsibleId = wo.getResponsible().getId();
		operationName = wo.getOperation().getName();
		responsibleName = wo.getResponsible().getName();
		area = wo.getCrop().getArea();
		table = wo.getCrop().getField().getName();
		cropName = wo.getCrop().getName();
		year = wo.getCrop().getYear();
		
		this.materials = new ArrayList<MaterialDTO>();
		for(Material m : wo.getMaterials()) {
			MaterialDTO mat = new MaterialDTO(m);
			this.materials.add(mat);
		}
		
		this.machines = new ArrayList<MachineDTO>();
		for(Machine m : wo.getMachines()) {
			MachineDTO mac = new MachineDTO(m);
			this.machines.add(mac);
		}
		
		this.workers = new ArrayList<EmployeeDTO>();
		for(Worker w : wo.getWorkers()) {
			EmployeeDTO user = new EmployeeDTO(w);
			this.workers.add(user);
		}
	}	
}
