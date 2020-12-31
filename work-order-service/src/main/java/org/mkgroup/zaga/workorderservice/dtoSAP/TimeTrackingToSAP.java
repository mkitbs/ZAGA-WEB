package org.mkgroup.zaga.workorderservice.dtoSAP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.model.TimeTrackingType;
import org.mkgroup.zaga.workorderservice.model.WorkerTimeTracking;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeTrackingToSAP {

	@JsonProperty("CompanyCode")
	private String companyCode;
	
	@JsonProperty("WorkOrderNumber")
	private String workOrderNumber;
	
	@JsonProperty("WorkOrderEmployeeNumber")
	private String workOrderEmployeeNumber;
	
	@JsonProperty("RecordType")
	private String recordType;
	
	@JsonProperty("StartTime")
	private String startTime;
	
	@JsonProperty("EndTime")
	private String endTime;
	
	@JsonProperty("Username")
	private String username;
	
	@JsonProperty("RecordNumber")
	private String recordNumber;
	
	@JsonProperty("StartDate")
	private String startDate;
	
	@JsonProperty("EndDate")
	private String endDate;
	
	public TimeTrackingToSAP(WorkerTimeTracking wtt, String companyCode) {
		this.companyCode = companyCode;
		this.workOrderNumber = wtt.getWorkOrderWorker().getWorkOrder().getErpId().toString();
		this.workOrderEmployeeNumber = String.valueOf(wtt.getWorkOrderWorker().getErpId());
		if(wtt.getType().equals(TimeTrackingType.RN)) {
			this.recordType = "1";
		} else if(wtt.getType().equals(TimeTrackingType.PAUSE_WORK)) {
			this.recordType = "2";
		} else if(wtt.getType().equals(TimeTrackingType.PAUSE_SERVICE)) {
			this.recordType = "3";
		} else if(wtt.getType().equals(TimeTrackingType.PAUSE_FUEL)) {
			this.recordType = "4";
		} else if(wtt.getType().equals(TimeTrackingType.FINISHED)) {
			this.recordType = "5";
		}
		String startTime2Send = "";
		String endTime2Send = "";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if(wtt.getStartTime() != null) {
			String strStartTime = sdf.format(wtt.getStartTime());
			String startDate = dateFormat.format(wtt.getStartTime());
			startTime2Send += "PT" + strStartTime.split(":")[0] + 
					"H" + strStartTime.split(":")[1] + 
					"M" + strStartTime.split(":")[2] +
					"S";
			this.startTime = startTime2Send;
			this.startDate = startDate;
		} else {
			this.startTime = "PT00H00M00S";
			this.startDate = "";
		}
		
		if(wtt.getEndTime() != null) {
			String strEndTime = sdf.format(wtt.getEndTime());
			String endDate = dateFormat.format(wtt.getEndTime());
			endTime2Send += "PT" + strEndTime.split(":")[0] + 
					"H" + strEndTime.split(":")[1] + 
					"M" + strEndTime.split(":")[2] +
					"S";
			this.endTime = endTime2Send;
			this.endDate = endDate;
		} else {
			this.endTime = "PT00H00M00S";
		}
		
		this.username = "MKATIC";
		if(String.valueOf(wtt.getErpId()) != null) {
			this.recordNumber = String.valueOf(wtt.getErpId());
		}
	}
}
