package org.mkgroup.zaga.authorizationservice.dto;

import java.util.ArrayList;
import java.util.List;

import org.mkgroup.zaga.authorizationservice.model.Role;
import org.mkgroup.zaga.authorizationservice.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
	String sapUserId;

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
		this.sapUserId = u.getSapUserId();
		roles = new ArrayList<>();
		if(u.getRoles() != null) {
			for(Role r : u.getRoles()) {
				roles.add(new RoleDTO(r.getName()));
			}
		}
	}
	
}
