package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.FieldGroupDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "field_group", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class FieldGroup {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private String companyCode;
	
	private String orgUnit;
	
	private Long erpId;
	
	@OneToMany(mappedBy = "fieldGroup")
	private List<Field> fields;
	
	public FieldGroup(FieldGroupDTO field) {
		this.name = field.getName();
		this.companyCode = field.getCompanyCode();
		this.orgUnit = field.getOrgUnit();
		this.erpId = field.getId();
	}

}
