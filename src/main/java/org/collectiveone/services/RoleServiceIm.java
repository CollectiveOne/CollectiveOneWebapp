package org.collectiveone.services;

import java.util.List;

import org.collectiveone.model.Role;
import org.collectiveone.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceIm implements RoleServiceIf {

	@Autowired
	private RoleRepository roleRepository;
	
	public List<Role> getRolesOf(String username) {
		return roleRepository.findByUsername(username);
		
	}
	
	public void addRoleTo(String username, String roleName) {
		/* add role if not yet added */
		if(roleRepository.findByUsernameAndRole(username, roleName) == null) {
			Role role = new Role();
			role.setUsername(username);
			role.setRole(roleName);
			
			roleRepository.save(role);
		}
	}
	
	public Role save(Role role) {
		return roleRepository.save(role);
		
	}
	
}
