package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;

import org.mkgroup.zaga.workorderservice.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

	List<Material> findByOrderByNameAsc();
}
