package org.mkgroup.zaga.authorizationservice.dto;

import java.util.HashSet;
import java.util.Set;

import org.mkgroup.zaga.authorizationservice.model.Permission;
import org.mkgroup.zaga.authorizationservice.model.Role;
import org.mkgroup.zaga.authorizationservice.model.RoleName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

	private Long id;
	private RoleName name;
	private Set<PermissionDTO> permissions = new HashSet<PermissionDTO>();

    public RoleDTO(RoleName name) {
        this.name = name;
    }
    
    public RoleDTO(Role role) {
    	this.id = role.getId();
    	this.name = role.getName();
    	for(Permission p : role.getPermissions()) {
    		PermissionDTO dto = new PermissionDTO(p);
    		permissions.add(dto);
    	}
    }
}
