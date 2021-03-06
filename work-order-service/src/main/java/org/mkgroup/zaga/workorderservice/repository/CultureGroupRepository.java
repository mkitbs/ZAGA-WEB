package org.mkgroup.zaga.workorderservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.CultureGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CultureGroupRepository extends JpaRepository<CultureGroup, UUID>{
	
	Optional<CultureGroup> findByErpId(Long id);
}
