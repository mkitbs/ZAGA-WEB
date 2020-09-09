package org.mkgroup.zaga.workorderservice.repository;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long>{

	Crop findById(UUID id);
	
	
}
