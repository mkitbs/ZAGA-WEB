package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class WorkOrderMachine {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="worker_id", nullable=true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="machine_id", nullable=true)
	private Machine machine;
	
	@ManyToOne
	@JoinColumn(name="worke_order_id", nullable=true)
	private WorkOrder workOrder;
	
	private double initialState;
	
	private double finalState;
	
	private double sumState;
	
	private double workPeriod;
	
	private Date date;
}
