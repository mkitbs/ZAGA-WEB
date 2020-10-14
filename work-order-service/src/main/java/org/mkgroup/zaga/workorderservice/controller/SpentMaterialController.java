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
import org.mkgroup.zaga.workorderservice.service.SpentMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@Autowired
	SpentMaterialService spentMaterialService;
	
	@PostMapping("{id}")
	public ResponseEntity<?> addSpentMaterial(@PathVariable UUID id, @RequestBody SpentMaterialDTO spentMaterialDTO){
		spentMaterialService.addSpentMaterial(id, spentMaterialDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("updateSpentMaterial/{id}")
	public ResponseEntity<?> updateSpentMaterial(@PathVariable UUID id, @RequestBody SpentMaterialDTO spentMaterialDTO){
		spentMaterialService.updateSpentMaterial(id, spentMaterialDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("deleteSpentMaterial/{id}")
	public ResponseEntity<?> deleteSpentMaterial(@PathVariable UUID id){
		spentMaterialService.deleteSpentMaterial(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
