package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.OperationsTodayDTO;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID>{

	@Query(value = "SELECT * FROM work_order w ORDER BY w.creation_date ASC", nativeQuery = true)
	List<WorkOrder> findAllOrderByCreationDate();
	
	@Query(value = "SELECT * FROM work_order w WHERE w.user_created_sap_id=?1 AND w.tenant_id=?2 ORDER BY w.creation_date ASC", nativeQuery = true)
	List<WorkOrder> findMyOrderByCreationDate(Long sapUserId, Long tenantId);
	
	@Query(value = "SELECT * FROM work_order w WHERE w.tenant_id=?1 AND w.status=?2", nativeQuery = true)
	List<WorkOrder> findWoByStatus(Long tenantId, String status);
	
	@Query(value = "SELECT * FROM work_order w WHERE w.tenant_id=?1 AND w.user_created_sap_id=?2 AND w.status=?3", nativeQuery = true)
	List<WorkOrder> findMyWoByStatus(Long tenantId, Long sapUserId, String status);
	
	@Query(value = "SELECT op.name AS operation, SUM(wo.treated) AS treated, SUM(co.area-wo.treated) AS area FROM work_order AS wo INNER JOIN operation AS op ON wo.operation_id=op.id INNER JOIN crop AS co ON wo.crop_id=co.id WHERE wo.tenant_id=?1 AND wo.date=curdate() GROUP BY wo.operation_id", nativeQuery = true)
	List<OperationsTodayDTO> findAllOperationsForToday(Long tenantId);
}
