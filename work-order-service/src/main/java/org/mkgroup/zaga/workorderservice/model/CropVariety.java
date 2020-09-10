package org.mkgroup.zaga.workorderservice.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class CropVariety {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UUID.randomUUID();
	
	private String companyCode;
	
	private String orgUnit;
	
	private double area;
	
	@ManyToOne
	private Crop crop;
	
	@ManyToOne
	private Variety variety;
}