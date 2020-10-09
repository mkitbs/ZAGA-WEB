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
		//System.out.println(wo.getDate().toString());
		//dayWithHours = wo.getDate().toString().split("-")[2];
		day = "1";
		month = "1";
		year = "1";
	}
}
