package org.mkgroup.zaga.workorderservice.dtoSAP;

import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderToSAP {

	@JsonProperty("Activity")
	private String Activity;
	@JsonProperty("CompanyCode")
	private String CompanyCode;
	@JsonProperty("WorkOrderNumber")
	private String WorkOrderNumber;
	@JsonProperty("WorkItemNumber")
	private String WorkItemNumber;
	@JsonProperty("OrganisationUnit")
	private String OrganisationUnit;
	@JsonProperty("WorkOrderDate")
	private String WorkOrderDate;
	@JsonProperty("WorkOrderOpenDate")
	private String WorkOrderOpenDate;
	@JsonProperty("WorkOrderCloseDate")
	private String WorkOrderCloseDate;
	@JsonProperty("ResponsibleUserNumber")
	private String ResponsibleUserNumber;
	@JsonProperty("ReleasedUserNumber")
	private String ReleasedUserNumber;
	@JsonProperty("CropId")
	private String CropId;
	@JsonProperty("CropVarietyId")
	private String CropVarietyId;
	@JsonProperty("OperationId")
	private String OperationId;
	@JsonProperty("NoMaterial")
	private String NoMaterial;
	@JsonProperty("OnlyMaterial")
	private String OnlyMaterial;
	@JsonProperty("Note")
	private String Note;
	@JsonProperty("DataEntryUserNumber")
	private String DataEntryUserNumber;
	@JsonProperty("DataChangeUserNumber")
	private String DataChangeUserNumber;
	@JsonProperty("Deleted")
	private String Deleted;
	@JsonProperty("WorkOrderToMaterialNavigation")
	private WorkOrderToMaterialNavigation WorkOrderToMaterialNavigation = new WorkOrderToMaterialNavigation();;
	@JsonProperty("WorkOrderToEmployeeNavigation")
	private WorkOrderToEmployeeNavigation WorkOrderToEmployeeNavigation = new WorkOrderToEmployeeNavigation();
	@JsonProperty("WorkOrderToReturnNavigation")
	private WorkOrderToReturnNavigation WorkOrderToReturnNavigation = new WorkOrderToReturnNavigation();


	@SuppressWarnings("deprecation")
	public WorkOrderToSAP(WorkOrder workOrder, String action) {
		this.Activity = action;
		this.CompanyCode = "1200";
		this.OrganisationUnit = "BIPR";
		this.WorkItemNumber = "001";
		this.WorkOrderNumber = (action.equals("MOD") ? workOrder.getErpId().toString() : "");
		this.CropVarietyId = "000000";
		this.DataChangeUserNumber = workOrder.getResponsible().getPerNumber().toString();//menjati
		this.WorkOrderDate = "2020-09-24T00:00:00";
		this.WorkOrderOpenDate = "2020-09-24T00:00:00";
		this.WorkOrderCloseDate = null;
		this.ResponsibleUserNumber = workOrder.getResponsible().getPerNumber().toString();
		this.ReleasedUserNumber = workOrder.getResponsible().getPerNumber().toString();
		this.CropId = workOrder.getCrop().getErpId().toString();
		this.OperationId = workOrder.getOperation().getErpId().toString();
		this.NoMaterial = (workOrder.getMaterials().size() == 0)? "X" : "";
		this.OnlyMaterial = "";
		this.Note = "";
		this.DataEntryUserNumber = workOrder.getResponsible().getPerNumber().toString();//menjati
		this.Deleted = "";
		
		for(SpentMaterial sp : workOrder.getMaterials()) {
			MaterialToSAP mat = new MaterialToSAP();
			mat.setMaterialId(sp.getMaterial().getErpId().toString());
			mat.setPlannedQuantity(Double.toString(sp.getQuantity()));
			mat.setMaterialUnit(sp.getMaterial().getUnit());
			mat.setCharge("");
			mat.setDeleted("");
			mat.setSpentQuantity(sp.getSpent().toString());
			if(sp.getErpId() == 0) {
				mat.setWorkOrderMaterialNumber("");
			}else {
				mat.setWorkOrderMaterialNumber(String.valueOf(sp.getErpId()));
			}
			mat.setWebBackendId(sp.getId());
			
			this.WorkOrderToMaterialNavigation.getResults().add(mat);	
		}
		
		for(WorkOrderWorker wow : workOrder.getWorkers()) {
			WorkOrderEmployeeSAP woeSAP = new WorkOrderEmployeeSAP();
			if(wow.getErpId() != 0) {
				woeSAP.setWorkOrderEmployeeNumber(String.valueOf(wow.getErpId()));
			}else {
				woeSAP.setWorkOrderEmployeeNumber("");
			}
			woeSAP.setEmployeeId(wow.getUser().getPerNumber().toString());
			woeSAP.setOperationId(wow.getOperation().getErpId().toString());
			if(wow.getWorkPeriod() != -1 || wow.getNightPeriod() != -1) {
				Double sum = wow.getWorkPeriod() + wow.getNightPeriod();
				woeSAP.setWorkEffectiveHours(sum.toString());
			}else {
				woeSAP.setWorkEffectiveHours("0.00000");
			}
			woeSAP.setWorkSundayHours("0.00000");
			woeSAP.setWorkHolidayHours("0.00000");
			woeSAP.setOvertimeWork("");
			if(workOrder.getTreated() == 0) {
				woeSAP.setOperationOutput("0.0");
				woeSAP.setMachineAreaOutput("0.00000");
			}else {
				woeSAP.setMachineAreaOutput(Double.toString(workOrder.getTreated()));
				woeSAP.setOperationOutput(Double.toString(workOrder.getTreated()));
			}
			woeSAP.setOperationOutput(Double.toString(workOrder.getTreated()));
			woeSAP.setOperationOutputUnit("");
			woeSAP.setNoOperationOutput("");
			woeSAP.setMasterMachineId(wow.getMachine().getErpId().toString());
			woeSAP.setSlaveMachineId(wow.getConnectingMachine().getErpId().toString());
			woeSAP.setMachineTimeStart(wow.getInitialState().toString());
			woeSAP.setMachineTimeEnd(wow.getFinalState().toString());
			woeSAP.setSpentFuel(wow.getFuel().toString());
			if(wow.getNightPeriod() == -1) {
				woeSAP.setWorkNightHours("0.00000");
			}else {
				woeSAP.setWorkNightHours(wow.getNightPeriod().toString());
			}
			if(wow.getFinalState() != -1 && wow.getInitialState() != -1) {
				Double doub = wow.getFinalState() - wow.getInitialState();
				woeSAP.setMachineTime(doub.toString());
				woeSAP.setMachineEffectiveHours(doub.toString());
			}else {
				woeSAP.setMachineTime("0.00000");
				woeSAP.setMachineEffectiveHours("0.00000");
			}
			woeSAP.setMachineTime("0.0");
			woeSAP.setDeleted("");
			woeSAP.setWebBackendId(wow.getId());
			this.WorkOrderToEmployeeNavigation.getResults().add(woeSAP);
		}
		
	}

}
