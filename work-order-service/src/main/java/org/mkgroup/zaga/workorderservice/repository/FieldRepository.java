package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, UUID>{
	
	Optional<Field> findByErpId(Long id);
	List<Field> findByOrderByErpIdAsc();
}
