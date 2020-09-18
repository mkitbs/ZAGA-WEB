package org.mkgroup.zaga.workorderservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.FieldGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldGroupRepository extends JpaRepository<FieldGroup, UUID>{

	Optional<FieldGroup> findByErpId(Long id);
}
