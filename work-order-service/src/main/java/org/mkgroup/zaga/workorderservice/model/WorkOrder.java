package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class WorkOrder {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private Date startDate;
	
	private Date endDate;
	
	@Enumerated(EnumType.STRING)
	private WorkOrderStatus status;
	
	@ManyToOne
	private Operation operation;
	
	@ManyToOne
	private Crop crop;
	
	@ManyToMany
	private List<Worker> workers;
	
	
	
	
}
