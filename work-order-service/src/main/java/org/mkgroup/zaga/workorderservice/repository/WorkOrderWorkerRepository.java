package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkOrderWorkerRepository extends JpaRepository<WorkOrderWorker, UUID> {

	@Query(value = "SELECT * FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id WHERE wo.tenant_id=:tenantId ORDER BY wow.worker_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByWorkerId(@Param("tenantId") Long tenantId);
	
	@Query(value = "SELECT * FROM work_order_worker AS wow INNER JOIN work_order AS wo ON wow.work_order_id=wo.id WHERE wo.tenant_id=:tenantId ORDER BY wow.machine_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByMachineId(@Param("tenantId") Long tenantId);
	
}
