package org.mkgroup.zaga.workorderservice.dto;


import java.text.SimpleDateFormat;

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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(wo.getDate());
		System.out.println("DATEEEEE " + date + " WO IDDDD = " + wo.getErpId());
		//dayWithHours = wo.getDate().toString().split("-")[2];
		day = date.split("-")[2];
		month = date.split("-")[1];
		year = date.split("-")[0];
	}
}
