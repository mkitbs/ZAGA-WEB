package org.mkgroup.zaga.workorderservice.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
	private String name;
	
	private String department;
	
	private String position;
	
	private Long perNumber;
	
}
