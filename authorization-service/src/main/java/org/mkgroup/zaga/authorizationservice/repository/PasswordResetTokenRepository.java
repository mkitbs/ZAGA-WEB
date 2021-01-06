package org.mkgroup.zaga.authorizationservice.repository;

import java.util.Optional;

import org.mkgroup.zaga.authorizationservice.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);

	@Query(value = "SELECT * FROM password_reset_token prt WHERE prt.user_id=?1", nativeQuery = true)
	Optional<PasswordResetToken> findByUser(Long id);

}
