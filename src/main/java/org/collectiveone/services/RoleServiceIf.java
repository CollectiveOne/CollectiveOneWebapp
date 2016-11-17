package org.collectiveone.services;

import java.util.List;

import org.collectiveone.model.Role;

public interface RoleServiceIf {

	public List<Role> getRolesOf(String username);
	
	public void addRoleTo(String username, String roleName);
	
	public Role save(Role role);
	
}
