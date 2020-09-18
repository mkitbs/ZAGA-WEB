package org.mkgroup.zaga.workorderservice.model;

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
public class SpentMaterial {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private double quantity;
	
	private double spent;
	
	private double quantityPerHectar;
	
	private double spentPerHectar;
	
	@Column(columnDefinition = "BINARY(16)")
	private UUID materialId;
	
	@ManyToMany
	private List<ClosedWorkOrder> closedWorkOrder;
	
	
	
}
