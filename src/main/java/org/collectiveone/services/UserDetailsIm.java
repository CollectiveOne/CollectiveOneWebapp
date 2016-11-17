package org.collectiveone.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.collectiveone.model.Role;
import org.collectiveone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsIm implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private RoleServiceIf roleService;
	
	public RoleServiceIf getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleServiceIf roleService) {
		this.roleService = roleService;
	}

	private User user;
	
	
	public UserDetailsIm(){
	}
	
	public UserDetailsIm(User user){
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		
		List<Role> roles = roleService.getRolesOf(user.getUsername());
		for( Role role : roles ) {
			authorities.add( new SimpleGrantedAuthority(role.getRole()) ); 
		}
		return authorities;		
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getEnabled();
	}

}
