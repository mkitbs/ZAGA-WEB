package org.mkgroup.zaga.workorderservice.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class WorkOrder {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
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
	
	@ManyToMany
	private List<Machine> machines;
	
	@ManyToMany
	private List<Material> materials;

}
