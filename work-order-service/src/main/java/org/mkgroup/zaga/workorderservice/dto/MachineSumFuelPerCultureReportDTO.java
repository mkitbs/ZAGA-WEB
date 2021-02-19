package org.mkgroup.zaga.workorderservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineSumFuelPerCultureReportDTO {

	private String crop;
	private String culture;
	private List<MachineSumFuelDTO> machines;
}
