package org.mkgroup.zaga.workorderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderToSAP {

	private String Activity;
	private String CompanyCode;
	private String WorkOrderNumber;
	private String WorkItemNumber;
	private String OrganisationUnit;
	private String WorkOrderDate;
	private String WorkOrderOpenDate;
	private String WorkOrderCloseDate;
	private String WorkOrderValidFromDate;
	private String WorkOrderValidToDate;
	private String ResponsibleUserNumber;
	private String ReleasedUserNumber;
	private String CropId;
	private String CropVarietyId;
	private String OperationId;
	private String NoMaterial;
	private String OnlyMaterial;
	private String Note;
	private String DataEntryUserNumber;
	private String DataChangeUserNumber;
	private String Deleted;
}
