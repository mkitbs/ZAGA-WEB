package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;


import lombok.Data;

@Data
@Entity
public class ClosedWorkOrder {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
	private double treated;
	
	private UUID workOrderId;
	
	@ManyToMany
	private List<SpentMaterial> materials;
	
	@ManyToMany
	private List<MachineState> machines;
}
