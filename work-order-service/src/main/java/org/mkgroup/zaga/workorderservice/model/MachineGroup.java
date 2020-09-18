package org.mkgroup.zaga.workorderservice.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.mkgroup.zaga.workorderservice.dto.MachineGroupDTO;

import lombok.Data;

@Data
@Entity
public class MachineGroup {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;
	
	private String name;
	
	private Long erpId;
	
	@OneToMany(mappedBy = "machineGroupId")
	private List<Machine> machines;
	
	public MachineGroup(MachineGroupDTO machine) {
		this.name = machine.getName();
		this.erpId = machine.getErpId();
	}

}
