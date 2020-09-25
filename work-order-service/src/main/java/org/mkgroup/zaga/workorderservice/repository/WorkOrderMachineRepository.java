package org.mkgroup.zaga.workorderservice.repository;

import org.mkgroup.zaga.workorderservice.model.WorkOrderMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderMachineRepository extends JpaRepository<WorkOrderMachine, Long> {

}
