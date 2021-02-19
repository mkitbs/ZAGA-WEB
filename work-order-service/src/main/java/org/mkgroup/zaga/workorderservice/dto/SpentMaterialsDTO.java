package org.mkgroup.zaga.workorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpentMaterialsDTO {
	
	private double quantity;
	private String unit;
	private String material;
	
	public SpentMaterialsDTO(SpentMaterialPerCultureDTO spentMaterialPerCultureDTO) {
		this.quantity = spentMaterialPerCultureDTO.getQuantity();
		this.unit = spentMaterialPerCultureDTO.getUnit();
		this.material = spentMaterialPerCultureDTO.getMaterial();
	}

}
