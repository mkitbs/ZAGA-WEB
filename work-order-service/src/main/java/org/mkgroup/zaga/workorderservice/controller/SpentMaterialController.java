package org.mkgroup.zaga.workorderservice.controller;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spentMaterial/")
public class SpentMaterialController {

	@Autowired
	SpentMaterialRepository spentMaterialRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	MaterialRepository materialRepo;
	
	@PostMapping("{id}")
	public ResponseEntity<?> addSpentMaterial(@PathVariable UUID id, @RequestBody SpentMaterialDTO spentMaterialDTO){
		WorkOrder workOrder = workOrderRepo.getOne(id);
		
		SpentMaterial spentMaterial = spentMaterialRepo.getOne(spentMaterialDTO.getId());
		spentMaterial.setWorkOrder(workOrder);
		spentMaterial.setQuantity(spentMaterialDTO.getQuantity());
		spentMaterial.setQuantityPerHectar(spentMaterialDTO.getQuantity() / workOrder.getCrop().getArea());
		spentMaterial.setSpent(spentMaterialDTO.getSpent());
		spentMaterial.setSpentPerHectar(spentMaterialDTO.getSpent() / workOrder.getCrop().getArea());
		
		Material material = materialRepo.getOne(spentMaterialDTO.getMaterial().getId());
		spentMaterial.setMaterial(material);
		
		spentMaterialRepo.save(spentMaterial);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
