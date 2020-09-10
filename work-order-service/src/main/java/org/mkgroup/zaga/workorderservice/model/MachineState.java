package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;

@Data
@Entity
public class MachineState {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
	private double initialState;
	
	private double finalState;
	
	private double sumState;
	
	private double workPeriod;
	
	private Date date;
	
	private UUID machineId;
	
	private UUID workerId;
	
	@ManyToMany
	private List<ClosedWorkOrder> closedWorkOrder;
	
}
