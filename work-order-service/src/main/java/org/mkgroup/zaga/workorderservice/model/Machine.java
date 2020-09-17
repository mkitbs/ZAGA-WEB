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

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.MachineDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Machine {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private String companyCode;
	
	private String orgUnit;
	
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

		switch (m.getFuelType()) {
		case "0":
			this.fuelType = FuelType.NOT_SELECTED;
			break;
		case "1":
			this.fuelType = FuelType.GASOLINE;
			break;
		case "2":
			this.fuelType = FuelType.GAS;
			break;
		case "3":
			this.fuelType = FuelType.EURO_DIESEL;
			break;
		case "4":
			this.fuelType = FuelType.BIO_DIESEL;
			break;
		case "5":
			this.fuelType = FuelType.DIESEL;
			break;
		default:
			this.fuelType = FuelType.NOT_SELECTED;
			break;
		}
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
