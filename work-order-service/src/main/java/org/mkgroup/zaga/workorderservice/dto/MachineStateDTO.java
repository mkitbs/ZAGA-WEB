package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Machine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineStateDTO {
	
	private UUID machineId;
	private UUID workerId;
	
	public MachineStateDTO(Machine m) {
		machineId = m.getId();
	}
}
