package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private UUID userId;
	private String name;
	private Long perNumber;
	
	public UserDTO(User user) {
		this.userId = user.getId();
		this.name = user.getName();
		this.perNumber = user.getPerNumber();
	}
}
