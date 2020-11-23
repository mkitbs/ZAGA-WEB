package org.mkgroup.zaga.authorizationservice.repository;

import org.mkgroup.zaga.authorizationservice.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{
	
	Permission findByName(String name);

	boolean existsByName(String name);

}
