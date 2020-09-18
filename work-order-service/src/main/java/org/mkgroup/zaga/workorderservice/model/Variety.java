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
import org.mkgroup.zaga.workorderservice.dto.VarietyDTO;

import lombok.Data;

@Data
@Entity
@Table(name = "variety", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class Variety {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private String manufacturer;
	
	private String finishing;
	
	private String protection;
	
	private Long erpId;
	
	@ManyToOne
	private Culture culture;
	
	@OneToMany(mappedBy = "variety")
	private List<CropVariety> cropVariety;
	
	public Variety(VarietyDTO variety) {
		name = variety.getName();
		manufacturer = variety.getManufacturer();
		finishing = variety.getFinishing();
		protection = variety.getProtection();
		erpId = variety.getId();
	}
}
