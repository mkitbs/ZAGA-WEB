package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderWorkerRepository extends JpaRepository<WorkOrderWorker, UUID> {

	@Query(value = "SELECT * FROM work_order_worker w ORDER BY w.worker_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByWorkerId();
	
	@Query(value = "SELECT * FROM work_order_worker w ORDER BY w.machine_id", nativeQuery = true)
	List<WorkOrderWorker> findAllByOrderByMachineId();
}
