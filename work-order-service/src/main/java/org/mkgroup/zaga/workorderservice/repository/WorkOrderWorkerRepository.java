package org.mkgroup.zaga.workorderservice.repository;

import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderWorkerRepository extends JpaRepository<WorkOrderWorker, Long> {

}
