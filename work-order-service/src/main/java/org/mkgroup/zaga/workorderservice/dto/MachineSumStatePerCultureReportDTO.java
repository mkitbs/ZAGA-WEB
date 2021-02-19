package org.mkgroup.zaga.workorderservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineSumStatePerCultureReportDTO {

	private String crop;
	private String culture;
	private List<MachineSumStateDTO> machines;
}
