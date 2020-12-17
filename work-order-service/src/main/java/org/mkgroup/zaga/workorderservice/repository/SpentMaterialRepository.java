package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpentMaterialRepository extends JpaRepository<SpentMaterial, UUID>{

	@Query(value = "SELECT * FROM spent_material AS sm INNER JOIN work_order AS wo ON sm.work_order_id=wo.id WHERE wo.tenant_id=:tenantId ORDER BY sm.material_id", nativeQuery = true)
	List<SpentMaterial> findAllByOrderByMaterialId(@Param("tenantId") Long tenantId);
}
