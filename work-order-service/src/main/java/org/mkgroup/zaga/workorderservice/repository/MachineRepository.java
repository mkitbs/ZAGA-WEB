package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, UUID> {

	List<Machine> findByOrderByNameAsc();
}
