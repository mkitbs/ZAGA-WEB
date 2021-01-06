package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import javax.persistence.ManyToOne;

import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpentMaterialDTO {
	
	private UUID id;
	
	private Double quantity;
	
	private Double spent;
	
	private Double quantityPerHectar;
	
	private Double spentPerHectar;
	
	private MaterialDTO material;
	
	private boolean deleted;
	
	private boolean isFuel;
	
	public SpentMaterialDTO(SpentMaterial sm) {
		this.id = sm.getId();
		this.quantity = sm.getQuantity();
		this.spent = sm.getSpent();
		this.quantityPerHectar = sm.getQuantityPerHectar();
		this.spentPerHectar = sm.getSpentPerHectar();
		this.material = new MaterialDTO(sm.getMaterial());
		this.deleted = sm.isDeleted();
		this.isFuel = sm.isFuel();
	}
}
