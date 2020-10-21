package org.mkgroup.zaga.searchservice.model;

import java.io.Serializable;
import java.util.UUID;

import org.mkgroup.zaga.searchservice.dto.NewUserDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "user", type = "user", replicas = 0)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserElastic implements Serializable {

	public UserElastic(NewUserDTO user) {
		this.userId = user.getUserId();
		this.name = user.getName();
		this.perNumber = user.getPerNumber();
		this.position = user.getPosition();
	}

	private static final long serialVersionUID = -1090689451662115507L;
	
	@Id
	private String id;
	
	private UUID userId;
	
	private String name;
	
	private String department;
	
	private String position;
	
	private Long perNumber;
	
	private String _class;

}
