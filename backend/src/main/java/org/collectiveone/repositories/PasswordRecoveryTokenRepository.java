package org.collectiveone.repositories;

import org.collectiveone.model.PasswordRecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {

	PasswordRecoveryToken findByToken(String token);
}
