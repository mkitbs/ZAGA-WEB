package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperationRepository extends JpaRepository<Operation, UUID>{

	List<Operation> findByOrderByErpIdAsc();
	Optional<Operation> findByErpId(Long id);
	
	@Query(value = "SELECT * FROM operation o WHERE o.type=:type AND o.operation_group_id=:groupId", nativeQuery = true)
	List<Operation> findAllByTypeAndGroup(@Param("type") String type, @Param("groupId") UUID groupId);
	
	@Query(value = "SELECT * FROM operation o WHERE o.type=:type", nativeQuery = true)
	List<Operation> findByType(@Param("type") String type);
	
	@Query(value = "SELECT * FROM operation o WHERE o.operation_group_id=:groupId", nativeQuery = true)
	List<Operation> findAllByGroup(@Param("groupId") UUID groupId);
	
	@Query(value = "SELECT * FROM operation o GROUP BY o.type", nativeQuery = true)
	List<Operation> findAllByGroupByType();
}
