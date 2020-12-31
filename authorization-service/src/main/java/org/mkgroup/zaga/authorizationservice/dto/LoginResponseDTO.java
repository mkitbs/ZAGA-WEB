package org.mkgroup.zaga.authorizationservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
	
    private String ac_id;
    private List<RoleDTO> roles;
}
