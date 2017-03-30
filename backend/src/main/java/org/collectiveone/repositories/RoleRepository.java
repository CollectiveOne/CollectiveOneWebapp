package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> { // NO_UCD (test only)

	List<Role> findByUsername(String username); // NO_UCD (unused code)
	
    Role findByUsernameAndRole(String username, String role); // NO_UCD (unused code)

}
