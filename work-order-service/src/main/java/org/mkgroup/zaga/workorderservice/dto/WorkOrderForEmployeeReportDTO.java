package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;

public interface WorkOrderForEmployeeReportDTO {
	
	String getSapId();
	String getAtm();
	Date getWorkOrderDate();
	String getCrop();
	String getField();
	String getDayWork();
	String getNightWork();
	String getWoStatus();
}
