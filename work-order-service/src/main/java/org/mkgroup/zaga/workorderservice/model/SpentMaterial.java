package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class SpentMaterial {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
	private double quantity;
	
	private double spent;
	
	private double quantityPerHectar;
	
	private double spentPerHectar;
	
	@ManyToMany
	private List<ClosedWorkOrder> closedWorkOrder;
	
	
	@ManyToOne
	private Material material;
	
}
