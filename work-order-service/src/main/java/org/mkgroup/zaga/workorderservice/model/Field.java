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
import org.mkgroup.zaga.workorderservice.dto.FieldDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "field", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class Field {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String companyCode;
	
	private String orgUnit;
	
	private String name;
	
	private int year;
	
	private double area;
	
	private Long erpId;
	
	@ManyToOne
	private FieldGroup fieldGroup;
	
	@OneToMany(mappedBy = "field")
	private List<Crop> crops;
	
	public Field(FieldDTO field) {
		this.companyCode = field.getCompanyCode();
		this.orgUnit = field.getOrgUnit();
		this.name = field.getName();
		this.year = field.getYear();
		this.area = field.getArea();
	}
}
