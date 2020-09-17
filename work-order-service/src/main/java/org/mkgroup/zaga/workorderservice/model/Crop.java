package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.CropDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "crop", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class Crop {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private String companyCode;
	
	private String orgUnit;
	
	private Long erpId;
	
	private int year;
	
	private double area;
	
	private Long fieldId;
	
	@ManyToOne
	private Culture culture;
	
	@OneToMany(mappedBy = "crop")
	private List<CropVariety> cropVariety;
	
	@OneToMany(mappedBy = "crop")
	private List<WorkOrder> workOrders;
	
	public Crop(CropDTO crop) {
		name = crop.getName();
		companyCode = crop.getCompanyCode();
		orgUnit = crop.getOrganisationUnit();
		year = crop.getYear();
		erpId = crop.getErpId();
		area = crop.getArea();
		fieldId = crop.getFieldId();
	}
}
