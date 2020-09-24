package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class WorkOrderWorker {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name="worker_id", nullable=true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="operation_id", nullable=true)
	private Operation operation;
	
	@ManyToOne
	@JoinColumn(name="worke_order_id", nullable=true)
	private WorkOrder workOrder;
	
	private Date date;
	
	private double dayWorkPeriod;
	
	private double dayNightPeriod;
	
	private double workPeriod;
}
