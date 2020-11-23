package org.mkgroup.zaga.authorizationservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

	  @NotBlank
	  @Email(message="Nije validan format email-a.")
	  private String email;

	  @NotBlank
	  private String password;
}
