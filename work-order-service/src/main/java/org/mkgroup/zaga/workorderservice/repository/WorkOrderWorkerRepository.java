package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.mkgroup.zaga.workorderservice.dto.NumOfEmployeesPerOperationDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderTractorDriverDTO;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkOrderWorkerRepository extends JpaRepository<WorkOrderWorker, UUID> {

	@Query(value = "SELECT * FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id WHERE wo.tenant_id=:tenantId AND wow.deleted=false ORDER BY wow.worker_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByWorkerId(@Param("tenantId") Long tenantId);
	
	@Query(value = "SELECT * FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id WHERE wo.tenant_id=:tenantId AND wow.deleted=false ORDER BY wow.machine_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByMachineId(@Param("tenantId") Long tenantId);
	
	@Query(value = "SELECT * FROM work_order_worker w ORDER BY w.machine_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByMachineId();

	@Modifying
	@Transactional
	@Query(value = "UPDATE work_order_worker SET deleted = true WHERE id = ?1", nativeQuery = true)
	void deleteWorker(UUID id);
	
	@Query(value = "SELECT * FROM work_order_worker AS wow WHERE wow.worker_id=?1 AND wow.deleted=false", nativeQuery = true)
	List<WorkOrderWorker> findAllWoWByWorker(UUID workerId);
	
	@Query(value = "SELECT op.name AS operation, count(*) AS numOfEmployees FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id INNER JOIN operation AS op ON wow.operation_id=op.id WHERE wow.deleted=false AND wo.tenant_id=:tenantId AND wo.date=curdate() GROUP BY wow.operation_id", nativeQuery = true)
	List<NumOfEmployeesPerOperationDTO> findNumOfOperation(@Param("tenantId") Long tenantId);

}
