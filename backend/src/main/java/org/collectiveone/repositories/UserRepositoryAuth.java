package org.collectiveone.repositories;

import org.collectiveone.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryAuth extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
	User findByEmail(String email);
	
}
