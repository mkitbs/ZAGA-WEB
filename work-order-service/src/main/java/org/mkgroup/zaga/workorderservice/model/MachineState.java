package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class MachineState {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private double initialState;
	
	private double finalState;
	
	private double sumState;
	
	private double workPeriod;
	
	private Date date;
	
	@Column(columnDefinition = "BINARY(16)")
	private UUID machineId;
	
	@Column(columnDefinition = "BINARY(16)")
	private UUID workerId;
	
	@ManyToMany
	private List<ClosedWorkOrder> closedWorkOrder;
	
}
