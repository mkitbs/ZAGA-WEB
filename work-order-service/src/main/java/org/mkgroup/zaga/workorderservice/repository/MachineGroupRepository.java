package org.mkgroup.zaga.workorderservice.repository;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.MachineGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineGroupRepository extends JpaRepository<MachineGroup, UUID>{

	
}
