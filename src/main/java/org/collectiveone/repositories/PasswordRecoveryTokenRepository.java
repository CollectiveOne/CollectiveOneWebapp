package org.collectiveone.repositories;

import java.util.Date;
import java.util.stream.Stream;

import org.collectiveone.model.PasswordRecoveryToken;
import org.collectiveone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {

	PasswordRecoveryToken findByToken(String token);

	PasswordRecoveryToken findByUser(User user);

    Stream<PasswordRecoveryToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
