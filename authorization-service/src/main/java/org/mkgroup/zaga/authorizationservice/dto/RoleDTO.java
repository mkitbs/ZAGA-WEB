package org.mkgroup.zaga.authorizationservice.dto;

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


    public RoleDTO(RoleName name) {
        this.name = name;
    }
}
