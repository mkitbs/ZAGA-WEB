package org.mkgroup.zaga.workorderservice.dto;

import java.util.Date;

public interface WorkOrderForMaterialReportDTO {

	String getSapId();
	String getAtm();
	Date getWorkOrderDate();
	String getCrop();
	String getField();
	String getQuantity();
	String getSpent();
	String getWoStatus();
}
