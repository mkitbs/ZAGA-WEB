package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID>{

	@Query(value = "SELECT * FROM work_order w ORDER BY w.creation_date ASC", nativeQuery = true)
	List<WorkOrder> findAllOrderByCreationDate();
	
	@Query(value = "SELECT * FROM work_order w WHERE w.user_created_sap_id=?1 ORDER BY w.creation_date ASC", nativeQuery = true)
	List<WorkOrder> findMyOrderByCreationDate(Long sapUserId);
	
	List<WorkOrder> findAllByStatus(WorkOrderStatus status);
}
