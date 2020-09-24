package org.mkgroup.zaga.workorderservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
	@JoinColumn(name="responsible_id", nullable=true)
	private User responsible;
	
	@ManyToOne
	@JoinColumn(name="operation_id", nullable=true)
	private Operation operation;
	
	@ManyToOne
	@JoinColumn(name="crop_id", nullable=true)
	private Crop crop;
	
	@ManyToMany(mappedBy = "workOrder")
	private List<WorkOrderWorker> workers;
	
	@OneToMany(mappedBy = "workOrder")
	private List<WorkOrderMachine> machines;
	
	@ManyToMany
	@JoinTable(
	  name = "assigned_users", 
	  joinColumns = @JoinColumn(name = "work_order_id"), 
	  inverseJoinColumns = @JoinColumn(name = "assigned_user_id"))
	private List<User> assignedUsers = new ArrayList<User>();
	
	@OneToMany(mappedBy = "workOrder")
	private List<SpentMaterial> materials;

}
