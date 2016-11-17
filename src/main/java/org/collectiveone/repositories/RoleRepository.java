package org.collectiveone.repositories;

import java.util.List;

import org.collectiveone.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	List<Role> findByUsername(String username);
	
    Role findByUsernameAndRole(String username, String role);

}
