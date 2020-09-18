package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.MachineDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "machine", uniqueConstraints = {@UniqueConstraint(columnNames ={"erpId"})})
public class Machine {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private String companyCode;
	
	private String orgUnit;
	
	private String erpId;
	
	@Enumerated(EnumType.STRING)
	private MachineType type;
	
	@Enumerated(EnumType.STRING)
	private FuelType fuelType;
	
	@Enumerated(EnumType.STRING)
	private OwnershipType ownershipType;
	
	@ManyToOne
	private MachineGroup machineGroupId;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<WorkOrder> workOrder;

	public Machine(MachineDTO m) {
		this.name = m.getName();
		this.companyCode = m.getCompanyCode();
		this.orgUnit = m.getOrgUnit();
		
		if(m.getType().equals("PG")) {
			this.type = MachineType.PROPULSION;
		} else {
			this.type = MachineType.COUPLING;
		}
		
		this.erpId = m.getErpId();
		this.fuelType = FuelType.values()[Integer.parseInt(m.getFuelType())];
		switch (m.getOwnershipType()) {
		case "S":
			this.ownershipType = OwnershipType.OWN;
			break;
		case "G":
			this.ownershipType = OwnershipType.IN_THE_GROUP;
			break;
		case "E":
			this.ownershipType = OwnershipType.EXTERNAL;
			break;
		default:
			break;
		}
	}
}
