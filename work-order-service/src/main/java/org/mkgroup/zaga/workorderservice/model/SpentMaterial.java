package org.mkgroup.zaga.workorderservice.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class SpentMaterial {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private Double quantity = -1.0;
	
	private Double spent = -1.0;
	
	private Double quantityPerHectar = -1.0;
	
	private Double spentPerHectar = -1.0;
	
	private boolean isFuel = false;
	
	@ManyToOne
	private Material material;
	
	@ManyToOne
	private WorkOrder workOrder;
	
	private int erpId = 0;
	
	private boolean deleted = false; //logicko brisanje zbog SAP-a
}
