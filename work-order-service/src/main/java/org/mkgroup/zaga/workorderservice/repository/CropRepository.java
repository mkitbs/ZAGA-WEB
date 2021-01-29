package org.mkgroup.zaga.workorderservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import feign.Param;

@Repository
public interface CropRepository extends JpaRepository<Crop, UUID>{
	
	List<Crop> findByOrderByErpIdAsc();
	Optional<Crop> findByErpId(Long id);
	
	@Query(value = "SELECT * FROM crop c WHERE c.field_id=:fieldId AND c.year=:year", nativeQuery = true)
	List<Crop> findByFieldAndYear(@Param("fieldId") UUID fieldId, @Param("year") int year);
	
	@Query(value = "SELECT * FROM crop c WHERE c.field_id=:fieldId AND c.culture_id=:cultureId", nativeQuery = true)
	List<Crop> findByFieldAndCulture(@Param("fieldId") UUID fieldId, @Param("cultureId") UUID cultureId);
	
	@Query(value = "SELECT * FROM crop c WHERE c.field_id=:fieldId", nativeQuery = true)
	List<Crop> findByField(UUID fieldId);
	
	@Query(value = "SELECT * FROM crop c WHERE c.culture_id=:cultureId", nativeQuery = true)
	List<Crop> findByCulture(UUID cultureId);
}
