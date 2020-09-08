package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class SpentMaterial {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private double quantity;
	
	private double spent;
	
	private double quantityPerHectar;
	
	private double spentPerHectar;
	
	@ManyToMany
	@JoinTable(
			  name = "spentMaterialOrder", 
			  joinColumns = @JoinColumn(name = "spentMaterial_id"), 
			  inverseJoinColumns = @JoinColumn(name = "closeWorkOrder_id"))
	private List<WorkOrder> workOrder;
	
	@ManyToOne
	Material material;
	
}
