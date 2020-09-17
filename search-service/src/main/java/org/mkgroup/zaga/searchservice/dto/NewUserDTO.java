package org.mkgroup.zaga.searchservice.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUserDTO {
	
	private UUID id;
	
	private String name;
	
	private String department;
	
	private String position;
	
	private Long perNumber;
	

}
