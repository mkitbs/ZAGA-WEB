package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.mkgroup.zaga.workorderservice.dto.NumOfEmployeesPerOperationDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialPerCultureDTO;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface SpentMaterialRepository extends JpaRepository<SpentMaterial, UUID>{


	@Query(value = "SELECT * FROM spent_material AS sm INNER JOIN work_order AS wo ON sm.work_order_id=wo.id WHERE wo.tenant_id=:tenantId AND sm.deleted=false ORDER BY sm.material_id", nativeQuery = true)
	List<SpentMaterial> findAllByOrderByMaterialId(@Param("tenantId") Long tenantId);
	
	List<SpentMaterial> findAllByOrderByMaterialId();
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE spent_material SET deleted = true WHERE id = ?1", nativeQuery = true)
	void deleteMaterial(UUID id);
	
	@Query(value = "SELECT * FROM spent_material sm WHERE sm.work_order_id=?1 AND sm.material_id=?2 AND sm.deleted=false AND sm.is_fuel=true", nativeQuery = true)
	Optional<SpentMaterial> findByWoAndMaterial(UUID woId, UUID materialId);
	
	@Query(value = "SELECT * FROM spent_material WHERE erp_id=?1 AND work_order_id=?2", nativeQuery = true)
	Optional<SpentMaterial> findByErpAndWorkOrder(int erpId, UUID workOrderId);
	
	@Query(value = "SELECT SUM(smat.quantity) AS quantity, mat.unit AS unit, mat.name AS material, c.name AS crop, cl.name AS culture FROM spent_material AS smat INNER JOIN material AS mat ON smat.material_id=mat.id INNER JOIN work_order AS wo ON smat.work_order_id=wo.id INNER JOIN crop AS c ON wo.crop_id=c.id INNER JOIN culture AS cl on c.culture_id=cl.id WHERE smat.quantity!=-1 AND smat.deleted=false AND wo.tenant_id=?1 GROUP BY wo.crop_id, smat.material_id ORDER BY c.name ASC", nativeQuery = true)
	List<SpentMaterialPerCultureDTO> getSpentMaterialPerCulture(Long tenantId);
}
