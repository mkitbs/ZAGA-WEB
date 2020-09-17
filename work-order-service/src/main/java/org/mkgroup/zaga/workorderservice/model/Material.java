package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.MaterialDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "material", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class Material {
	
	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private String unit;
	
	private String materialGroup;
	
	@Column(name = "erpId")
	private Long erpId;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<WorkOrder> workOrder;
	
	public Material(MaterialDTO m) {
		this.name = m.getName();
		this.unit = m.getUnit();
		this.erpId = m.getErpId();
		this.materialGroup = m.getGroup();
	}
}
