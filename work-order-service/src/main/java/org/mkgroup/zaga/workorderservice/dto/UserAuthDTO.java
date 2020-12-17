package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDTO {

	Long id;
	String name;
	String surname;
	Long addressId;
	String email;
	String password;
	boolean enabled;
	boolean nonLocked;
	List<RoleAuthDTO> roles;
	String telephone;
	String sapUserId;
	Date dateOfBirth;
	TenantAuthDTO tenant;
	
}
