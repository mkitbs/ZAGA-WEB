package org.mkgroup.zaga.workorderservice.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAuthDTO {

	private Long id;
	private RoleName name;
	private Set<PermissionAuthDTO> permissions = new HashSet<PermissionAuthDTO>();
    
}
