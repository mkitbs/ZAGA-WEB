package org.mkgroup.zaga.workorderservice.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.CropVarietyDTO;

import lombok.Data;

@Data
@Entity
@Table(name = "crop_variety", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class CropVariety {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String companyCode;
	
	private String orgUnit;
	
	private double area;
	
	private Long erpId;
	
	@ManyToOne
	private Crop crop;
	
	@ManyToOne
	private Variety variety;
	
	public CropVariety(CropVarietyDTO cropVariety) {
		this.companyCode = cropVariety.getCompanyCode();
		this.orgUnit = cropVariety.getOrganisationUnit();
		this.area = cropVariety.getArea();
		this.erpId = cropVariety.getId();
	}

}