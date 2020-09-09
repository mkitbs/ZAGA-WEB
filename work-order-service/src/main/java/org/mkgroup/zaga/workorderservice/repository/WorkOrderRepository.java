package org.mkgroup.zaga.workorderservice.repository;

import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>{

	
}
