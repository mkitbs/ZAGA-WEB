package org.mkgroup.zaga.workorderservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.OperationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationGroupRepository extends JpaRepository<OperationGroup, UUID>{
	
	Optional<OperationGroup> findByErpId(Long id);
}
