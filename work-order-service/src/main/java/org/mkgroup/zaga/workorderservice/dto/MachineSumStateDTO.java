package org.mkgroup.zaga.workorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineSumStateDTO {

	private double state;
	private String name;

	public MachineSumStateDTO(MachineSumStatePerCultureDTO machineSumStatePerCultureDTO) {
		this.state = machineSumStatePerCultureDTO.getState();
		this.name = machineSumStatePerCultureDTO.getMachine();
	}
}
