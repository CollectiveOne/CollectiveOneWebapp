package org.collectiveone.repositories;

import org.collectiveone.model.AuthorizedEmail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizedEmailRepository extends CrudRepository<AuthorizedEmail, Long> {

	public AuthorizedEmail findByEmailAndAuthorized(String email, Boolean authorized);
	
}
