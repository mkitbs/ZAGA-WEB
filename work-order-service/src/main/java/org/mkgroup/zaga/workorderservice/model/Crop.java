package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class Crop {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private String name;
	
	private String companyCode;
	
	private String orgUnit;
	
	private int season;
	
	private double area;
	
	private Long fieldId;
	
	@ManyToOne
	private Culture culture;
	
	@OneToMany(mappedBy = "crop")
	private List<CropVariety> cropVariety;
	
	@OneToMany(mappedBy = "crop")
	private List<WorkOrder> workOrders;
}
