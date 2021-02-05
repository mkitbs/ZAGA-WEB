package org.mkgroup.zaga.authorizationservice.repository;

import java.util.List;
import java.util.Optional;

import org.mkgroup.zaga.authorizationservice.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@EntityGraph(value = "User.Roles.Permissions")
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String username);

	@Query("select u from User u where u.id = ?#{principal.id}")
	User findCurrentUser();
	
	@Query("select u from User u where u.id != ?#{principal.id}")
	List<User> findUsersExceptSelf();
	
	User findBySapUserId(String sapId);
	
	boolean existsBySapUserIdAndTenantId(String sapUserId, Long tenantId);
}
