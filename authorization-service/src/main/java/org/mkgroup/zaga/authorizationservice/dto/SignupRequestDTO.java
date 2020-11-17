package org.mkgroup.zaga.authorizationservice.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {

	@NotBlank
	private String name;
	
	@NotBlank
	private String surname;
	
	private List<RoleDTO> roles;
	
	@NotBlank
	private String telephone;
	
	@Email
	@NotBlank
	@Size(min = 3)
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String confirmPassword;
}
