package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;

import org.mkgroup.zaga.workorderservice.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

	List<Machine> findByOrderByNameAsc();
}
