package org.mkgroup.zaga.workorderservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.EmployeeDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames ={"perNumber"})})
public class User {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private String department;
	
	private String position;
	
	@Column(name= "perNumber")
	private Long perNumber;
	
	@OneToMany(mappedBy = "responsible")
	private List<WorkOrder> responsible = new ArrayList<WorkOrder>();
	
	@OneToMany(mappedBy = "user")
	private List<WorkOrderMachine> workOrderMachines = new ArrayList<WorkOrderMachine>();
	
	@OneToMany(mappedBy = "user")
	private List<WorkOrderWorker> workOrderWorking = new ArrayList<WorkOrderWorker>();
	
	public User(EmployeeDTO em) {
		this.name = em.getName();
		this.department = em.getDepartment();
		this.position = em.getPosition();
		this.perNumber = em.getPerNumber();
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Long getPerNumber() {
		return perNumber;
	}

	public void setPerNumber(Long perNumber) {
		this.perNumber = perNumber;
	}

	public List<WorkOrder> getResponsible() {
		return responsible;
	}

	public void setResponsible(List<WorkOrder> responsible) {
		this.responsible = responsible;
	}

	public List<WorkOrderMachine> getWorkOrderMachines() {
		return workOrderMachines;
	}

	public void setWorkOrderMachines(List<WorkOrderMachine> workOrderMachines) {
		this.workOrderMachines = workOrderMachines;
	}

	public List<WorkOrderWorker> getWorkOrderWorking() {
		return workOrderWorking;
	}

	public void setWorkOrderWorking(List<WorkOrderWorker> workOrderWorking) {
		this.workOrderWorking = workOrderWorking;
	} 
	
	
}
