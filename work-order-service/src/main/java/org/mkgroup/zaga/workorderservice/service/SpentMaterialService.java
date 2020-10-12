package org.mkgroup.zaga.workorderservice.service;

import java.util.UUID;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpentMaterialService {
	
	private static final Logger log = Logger.getLogger(SpentMaterial.class);

	@Autowired
	SpentMaterialRepository spentMaterialRepo;
	
	@Autowired
	MaterialRepository materialRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	public SpentMaterial addSpentMaterial(SpentMaterial sm) {
		try {
			log.info("Insert spent material into db.");
			sm = spentMaterialRepo.save(sm);
			return sm;
		} catch(Exception e) {
			log.error("Insert spent material faild.", e);
			return null;
		}
	}
	
	public SpentMaterial getOne(UUID id) {
		try {
			SpentMaterial spentMaterial = spentMaterialRepo.getOne(id);
			return spentMaterial;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteSpentMaterial(UUID id) {
		try {
			spentMaterialRepo.deleteById(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addSpentMaterial(UUID id, SpentMaterialDTO spentMaterialDTO) {
		WorkOrder workOrder = workOrderRepo.getOne(id);
		
		SpentMaterial spentMaterial = new SpentMaterial();
		spentMaterial.setWorkOrder(workOrder);
		spentMaterial.setQuantity(spentMaterialDTO.getQuantity());
		spentMaterial.setQuantityPerHectar(spentMaterialDTO.getQuantity() / workOrder.getCrop().getArea());
		if(spentMaterialDTO.getSpent() != null) {
			spentMaterial.setSpent(spentMaterialDTO.getSpent());
			spentMaterial.setSpentPerHectar(spentMaterialDTO.getSpent() / workOrder.getCrop().getArea());
		} else {
			spentMaterial.setSpent(-1.0);
			spentMaterial.setSpentPerHectar(-1.0);
		}
		
		
		Material material = materialRepo.getOne(spentMaterialDTO.getMaterial().getId());
		spentMaterial.setMaterial(material);
		
		spentMaterialRepo.save(spentMaterial);
	}
	
	public void updateSpentMaterial(UUID id, SpentMaterialDTO spentMaterialDTO) {
		SpentMaterial spentMaterial = spentMaterialRepo.getOne(id);
		WorkOrder workOrder = workOrderRepo.getOne(spentMaterial.getWorkOrder().getId());
		
		spentMaterial.setQuantity(spentMaterialDTO.getQuantity());
		spentMaterial.setQuantityPerHectar(spentMaterialDTO.getQuantity() / workOrder.getCrop().getArea());
		if(spentMaterialDTO.getSpent() != null) {
			spentMaterial.setSpent(spentMaterialDTO.getSpent());
			spentMaterial.setSpentPerHectar(spentMaterialDTO.getSpent() / workOrder.getCrop().getArea());
		} else {
			spentMaterial.setSpent(-1.0);
			spentMaterial.setSpentPerHectar(-1.0);
		}
		
		Material material = materialRepo.getOne(spentMaterialDTO.getMaterial().getId());
		spentMaterial.setMaterial(material);
		
		spentMaterialRepo.save(spentMaterial);
	}
}
