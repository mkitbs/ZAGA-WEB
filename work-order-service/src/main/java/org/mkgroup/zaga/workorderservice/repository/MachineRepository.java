package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.MachineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, UUID> {

	List<Machine> findByOrderByNameAsc();
	Optional<Machine> findByErpId(String id);
	
	@Query(value = "select * from machine where type = ?1 order by name asc", nativeQuery = true)
	List<Machine> getMachines(String string);
}
