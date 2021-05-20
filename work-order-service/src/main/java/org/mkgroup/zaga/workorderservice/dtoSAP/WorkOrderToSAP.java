package org.mkgroup.zaga.workorderservice.dtoSAP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	@JsonProperty("NoteHeader")
	private String NoteHeader;
	@JsonProperty("NoteItem")
	private String NoteItem;
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
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        String workOpenDate = dateFormat.format(new Date());
        String workDate = dateFormat.format(workOrder.getDate());
		System.out.println(workOpenDate + " OPEN \n" + workDate + " WDATE");
		this.Activity = action;
		this.CompanyCode = "1200";
		this.OrganisationUnit = "PIKB";
		this.WorkItemNumber = "001";
		this.WorkOrderNumber = (action.equals("MOD") ? workOrder.getErpId().toString() : "");
		this.CropVarietyId = "000000";
		this.DataChangeUserNumber = workOrder.getUserCreatedSapId().toString();
		this.WorkOrderDate = workDate;
		this.WorkOrderOpenDate = workOpenDate;
		this.WorkOrderCloseDate = null;
		this.ResponsibleUserNumber = workOrder.getResponsible().getPerNumber().toString();
		this.ReleasedUserNumber = workOrder.getUserCreatedSapId().toString();
		this.CropId = workOrder.getCrop().getErpId().toString();
		this.OperationId = workOrder.getOperation().getErpId().toString();
		this.NoMaterial = (workOrder.getMaterials().size() == 0)? "X" : "";
		if(workOrder.getWorkers().size() == 0) {
			this.OnlyMaterial = "X";
		} else {
			this.OnlyMaterial = "";
		}
		this.NoteHeader = workOrder.getNumOfRefOrder();
		this.NoteItem = workOrder.getNote();
		this.DataEntryUserNumber = workOrder.getUserCreatedSapId().toString();
		this.Deleted = "";
		
		for(SpentMaterial sp : workOrder.getMaterials()) {
			MaterialToSAP mat = new MaterialToSAP();
			mat.setMaterialId(sp.getMaterial().getErpId().toString());
			if(sp.getQuantity() == -1) {
				mat.setPlannedQuantity("0.000");
			}else {
				mat.setPlannedQuantity(Double.toString(sp.getQuantity()));
			}
			
			mat.setMaterialUnit(sp.getMaterial().getUnit());
			mat.setCharge("");
			if(sp.isDeleted()) {
				mat.setDeleted("X");
			}else {
				mat.setDeleted("");
			}
			if(sp.getSpent() == -1) {
				mat.setSpentQuantity("0.000");
			}else {
				mat.setSpentQuantity(sp.getSpent().toString());
			}
			
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
			if(wow.getDayPeriod() != -1) {
				woeSAP.setWorkEffectiveHours(wow.getDayPeriod().toString());
			}else {
				woeSAP.setWorkEffectiveHours("0.00000");
			}
			woeSAP.setWorkSundayHours("0.00000");
			woeSAP.setWorkHolidayHours("0.00000");
			woeSAP.setOvertimeWork("");
			if (wow.isNoOperationOutput()) {
				woeSAP.setOperationOutput("0.00000");
				woeSAP.setMachineAreaOutput("0.00000");
			} else {
				if(wow.getOperationOutput() == null || wow.getOperationOutput() <= 0) {
					woeSAP.setOperationOutput("0.00000");
					woeSAP.setMachineAreaOutput("0.00000");
				} else {
					woeSAP.setMachineAreaOutput(Double.toString(wow.getOperationOutput()));
					woeSAP.setOperationOutput(Double.toString(wow.getOperationOutput()));
				}
			}
			woeSAP.setOperationOutputUnit("");
			if(wow.isNoOperationOutput()) {
				woeSAP.setNoOperationOutput("X");
			} else {
				woeSAP.setNoOperationOutput("");
			}
			woeSAP.setMasterMachineId(wow.getMachine().getErpId().toString());
			if(wow.getConnectingMachine() == null) {
				woeSAP.setSlaveMachineId("");
			}else {
				woeSAP.setSlaveMachineId(wow.getConnectingMachine().getErpId().toString());
			}
			
			if(wow.getInitialState() == -1) {
				woeSAP.setMachineTimeStart("0.0");
			}else {
				woeSAP.setMachineTimeStart(wow.getInitialState().toString());
			}
			if(wow.getFinalState() == -1) {
				woeSAP.setMachineTimeEnd("0.0");
			}else {
				woeSAP.setMachineTimeEnd(wow.getFinalState().toString());
			}
			if(wow.getFuel() == -1) {
				woeSAP.setSpentFuel("0.0");
			}else {
				woeSAP.setSpentFuel(wow.getFuel().toString());
			}
			
			if(wow.getNightPeriod() == -1) {
				woeSAP.setWorkNightHours("0.00000");
			}else {
				woeSAP.setWorkNightHours(wow.getNightPeriod().toString());
			}
			if(wow.getFinalState() != -1 && wow.getInitialState() != -1) {
				Double doub = wow.getFinalState() - wow.getInitialState();
				Double effectiveHours = wow.getDayPeriod() + wow.getNightPeriod();
				woeSAP.setMachineTime(doub.toString());
				woeSAP.setMachineEffectiveHours(effectiveHours.toString());
			}else {
				woeSAP.setMachineTime("0.00000");
				woeSAP.setMachineEffectiveHours("0.00000");
			}
			woeSAP.setMachineTime("0.0");
			if(wow.isDeleted()) {
				woeSAP.setDeleted("X");
			}else {
				woeSAP.setDeleted("");
			}
			woeSAP.setWebBackendId(wow.getId());
			//woeSAP.setNoOperationOutput("X");
			System.out.println("WOE SAP" + woeSAP);
			this.WorkOrderToEmployeeNavigation.getResults().add(woeSAP);
		}
		
	}

}
