package org.mkgroup.zaga.workorderservice.repository;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.CropVariety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropVarietyRepository extends JpaRepository<CropVariety, UUID>{

	
}
