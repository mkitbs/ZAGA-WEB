package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Culture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CultureRepository  extends JpaRepository<Culture, UUID>{

	List<Culture> findByOrderByNameAsc();
	Optional<Culture> findByErpId(Long id);
}

