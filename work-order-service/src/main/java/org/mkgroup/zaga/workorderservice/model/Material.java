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
public class Material {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
	private String name;
	
	private String unit;
	
	@ManyToMany
	private List<WorkOrder> workOrder;
	
}
