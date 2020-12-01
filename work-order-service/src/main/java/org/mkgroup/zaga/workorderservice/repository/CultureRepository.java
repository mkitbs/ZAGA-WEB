package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Culture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CultureRepository  extends JpaRepository<Culture, UUID>{

	List<Culture> findByOrderByNameAsc();
	Optional<Culture> findByErpId(Long id);
	
	@Query(value = "SELECT * FROM culture c WHERE c.org_con=:type", nativeQuery = true)
	List<Culture> findAllByOrgCon(@Param("type") String type);
	
	@Query(value = "SELECT * FROM culture c WHERE c.type=:type", nativeQuery = true)
	List<Culture> findAllByType(@Param("type") String type);
	
	@Query(value = "SELECT * FROM culture c WHERE c.culture_group_id=:id", nativeQuery = true)
	List<Culture> findAllByCultureGroup(@Param("id") UUID id);
	
	@Query(
			value = "SELECT * FROM culture c WHERE c.org_con=:orgCon AND c.type=:type AND c.culture_group_id=:id", 
			nativeQuery = true
	)
	List<Culture> findAllByOrgConCultureTypeCultureGroup(
			@Param("orgCon") String orgCon, @Param("type") String type, @Param("id") UUID id);
	
	@Query(value = "SELECT * FROM culture c WHERE c.org_con=:orgCon AND c.type=:type", nativeQuery = true)
	List<Culture> findAllByOrgConAndCultureType(@Param("orgCon") String orgCon, @Param("type") String type);
	
	@Query(value = "SELECT * FROM culture c WHERE c.org_con=:orgCon AND c.culture_group_id=:id", nativeQuery = true)
	List<Culture> findAllByOrgConAndCultureGroup(@Param("orgCon") String orgCon, @Param("id") UUID id);
	
	@Query(value = "SELECT * FROM culture c WHERE c.type=:type AND c.culture_group_id=:id", nativeQuery = true)
	List<Culture> findAllByCultureTypeAndCultureGroup(@Param("type") String type, @Param("id") UUID id);
	
	@Query(value = "SELECT * FROM culture c GROUP BY c.type", nativeQuery = true)
	List<Culture> findAllGroupByCultureType();
	
	@Query(value = "SELECT * FROM culture c GROUP BY c.org_con", nativeQuery = true)
	List<Culture> findAllGroupByProductionType();
}

