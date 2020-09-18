package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpentMaterialDTO {
	
	private UUID materialId;
	private double quantity;
	private double quantityPerHectar;
	
	public SpentMaterialDTO(Material m) {
		materialId = m.getId();
	}
}
