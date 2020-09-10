package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Variety {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
	private String name;
	
	private String manufacturer;
	
	private String finishing;
	
	private String protection;
	
	@ManyToOne
	private Culture culture;
	
	@OneToMany(mappedBy = "variety")
	private List<CropVariety> cropVariety;
	
	
}
