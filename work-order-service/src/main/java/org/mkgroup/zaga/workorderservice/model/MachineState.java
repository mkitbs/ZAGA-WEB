package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class MachineState {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
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
