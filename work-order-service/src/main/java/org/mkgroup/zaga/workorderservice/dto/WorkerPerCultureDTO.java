package org.mkgroup.zaga.workorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerPerCultureDTO {

	private double hour;
	private String name;
	
	public WorkerPerCultureDTO(SpentHourOfWorkerPerCultureDTO spentHourOfWorkerPerCultureDTO) {
		this.hour = spentHourOfWorkerPerCultureDTO.getHour();
		this.name = spentHourOfWorkerPerCultureDTO.getWorker();
	}
}
