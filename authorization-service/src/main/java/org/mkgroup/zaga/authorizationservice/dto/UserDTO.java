package org.mkgroup.zaga.authorizationservice.dto;

import java.util.ArrayList;
import java.util.List;

import org.mkgroup.zaga.authorizationservice.model.Role;
import org.mkgroup.zaga.authorizationservice.model.User;

public class UserDTO {

	Long id;
	String name;
	String surname;
	Long addressId;
	String email;
	String password;
	boolean enabled;
	boolean nonLocked;
	List<RoleDTO> roles;
	String telephone;
	

	public UserDTO(Long id, String name, String surname, Long address, String email, String password,
			boolean enabled, boolean nonLocked) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.addressId = address;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.nonLocked = nonLocked;
	}
	
	public UserDTO(User u) {
		this.id = u.getId();
		this.name = u.getName();
		this.surname = u.getSurname();
		this.email = u.getEmail();
		this.password = u.getPassword();
		this.enabled = u.isEnabled();
		this.nonLocked = u.isNonLocked();
		this.telephone = u.getTelephone();
		
		roles = new ArrayList<>();
		if(u.getRoles() != null) {
			for(Role r : u.getRoles()) {
				roles.add(new RoleDTO(r.getName()));
			}
		}
	}
}
