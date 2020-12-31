package org.mkgroup.zaga.authorizationservice.dto;

import java.util.ArrayList;
import java.util.List;

import org.mkgroup.zaga.authorizationservice.model.Tenant;
import org.mkgroup.zaga.authorizationservice.model.User;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantDTO {

	private Long id;
	private String name;
	private SettingDTO setting;
	private List<UserDTO> users;
	private String companyCode;
	
	public TenantDTO(Tenant t) {
		id = t.getId();
		name = t.getName();
		setting = new SettingDTO(t.getSetting());
		users = new ArrayList<UserDTO>();
		companyCode = t.getCompanyCode();
		for(User u : t.getUsers()) {
			users.add(new UserDTO(u.getSapUserId()));
		}
	}
}
