package org.mkgroup.zaga.workorderservice.model;

import java.io.Serializable;
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
public class WorkOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private Date startDate;
	
	private Date endDate;
	
	@Enumerated(EnumType.STRING)
	private WorkOrderStatus status;
	
	@ManyToOne
	private User responsible;
	
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