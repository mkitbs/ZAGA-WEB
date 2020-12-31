package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkerTimeTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerTimeTrackingRepository extends JpaRepository<WorkerTimeTracking, UUID>{

	@Query(value = "SELECT * FROM worker_time_tracking wt WHERE wt.work_order_worker_id=?1", nativeQuery = true)
	List<WorkerTimeTracking> findByWorkOrderWorkerId(UUID wowId);

}
