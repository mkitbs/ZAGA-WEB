package org.mkgroup.zaga.workorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineSumFuelDTO {

	private String name;
	private double fuel;
	
	public MachineSumFuelDTO(MachineSumFuelPerCultureDTO machineSumFuelPerCultureDTO) {
		this.name = machineSumFuelPerCultureDTO.getMachine();
		this.fuel = machineSumFuelPerCultureDTO.getFuel();
	}
}
