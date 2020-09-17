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
		
		if(m.getFuelType().equals("0")) {
			this.fuelType = FuelType.NOT_SELECTED;
		} else if(m.getFuelType().equals("1")){
			this.fuelType = FuelType.GASOLINE;
		} else if(m.getFuelType().equals("2")) {
			this.fuelType = FuelType.GAS;
		} else if(m.getFuelType().equals("3")) {
			this.fuelType = FuelType.EURO_DIESEL;
		} else if(m.getFuelType().equals("4")) {
			this.fuelType = FuelType.BIO_DIESEL;
		} else {
			this.fuelType = FuelType.DIESEL;
		}
		
		if(m.getType().equals("PG")) {
			this.type = MachineType.PROPULSION;
		} else {
			this.type = MachineType.COUPLING;
		}
		//dovrsiti
	}
}
