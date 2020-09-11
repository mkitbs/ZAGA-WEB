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

import lombok.Data;

@Data
@Entity
public class Machine {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String companyCode;
	
	private String orgUnit;
	
	@Enumerated(EnumType.STRING)
	private MachineType machineType;
	
	@Enumerated(EnumType.STRING)
	private FuelType fuelType;
	
	@Enumerated(EnumType.STRING)
	private OwnershipType ownershipType;
	
	@ManyToOne
	private MachineGroup machineGroup;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<WorkOrder> workOrder;

}
