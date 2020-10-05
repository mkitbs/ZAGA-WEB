package org.mkgroup.zaga.workorderservice.service;

import java.util.UUID;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpentMaterialService {
	
	private static final Logger log = Logger.getLogger(SpentMaterial.class);

	@Autowired
	SpentMaterialRepository spentMaterialRepo;
	
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
}
