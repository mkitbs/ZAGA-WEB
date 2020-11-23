package org.mkgroup.zaga.authorizationservice.repository;

import org.mkgroup.zaga.authorizationservice.model.Role;
import org.mkgroup.zaga.authorizationservice.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(RoleName name);
	boolean existsByName(RoleName name);
}
