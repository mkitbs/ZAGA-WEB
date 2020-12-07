package org.mkgroup.zaga.authorizationservice.dto;

import org.mkgroup.zaga.authorizationservice.model.Permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {

	private Long id;
	private String name;
	
	public PermissionDTO(Permission p) {
		this.id = p.getId();
		this.name = p.getName();
	}
}
