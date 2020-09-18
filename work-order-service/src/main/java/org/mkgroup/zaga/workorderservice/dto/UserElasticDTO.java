package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserElasticDTO {

	private UUID id;
	private String name;
	private String department;
	private String position;
	private Long perNumber;
	
	public UserElasticDTO(User u) {
		this.id =u.getId();
		this.department = u.getDepartment();
		this.name = u.getName();
		this.perNumber = u.getPerNumber();
		this.position = u.getPosition();
	}
}
