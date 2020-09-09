package org.mkgroup.zaga.workorderservice.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class CropVariety {
	
	/*
	@EmbeddedId
	private CropVarietyKey id;
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String companyCode;
	
	private String orgUnit;
	
	private double area;
	
	@ManyToOne
	private Crop crop;
	
	@ManyToOne
	private Variety variety;
}