package org.mkgroup.zaga.workorderservice.dto;


import org.mkgroup.zaga.workorderservice.model.WorkOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateDTO {
	
	private String day;
	private String month;
	private String year;
	private String dayWithHours;
	
	public DateDTO(WorkOrder wo) {
		dayWithHours = wo.getDate().toString().split("-")[2];
		day = dayWithHours.split(" ")[0];
		month = wo.getDate().toString().split("-")[1];
		year = wo.getDate().toString().split("-")[0];
	}
}
