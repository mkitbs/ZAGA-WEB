package org.mkgroup.zaga.workorderservice.dtoSAP;

import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;

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
	private WorkOrderToMaterialNavigation WorkOrderToMaterialNavigation = new WorkOrderToMaterialNavigation();;
	private WorkOrderToEmployeeNavigation WorkOrderToEmployeeNavigation = new WorkOrderToEmployeeNavigation();
	private WorkOrderToReturnNavigation WorkOrderToReturnNavigation = new WorkOrderToReturnNavigation();


	@SuppressWarnings("deprecation")
	public WorkOrderToSAP(WorkOrder workOrder) {
		this.Activity = "NEW";
		this.CompanyCode = "1200";
		this.OrganisationUnit = "BIPR";
		this.WorkItemNumber = "001";
		this.WorkOrderNumber = "";
		this.CropVarietyId = "000000";
		this.DataChangeUserNumber = workOrder.getResponsible().getPerNumber().toString();//menjati
		this.WorkOrderDate = "2020-09-24T00:00:00";
		this.WorkOrderOpenDate = "2020-09-24T00:00:00";
		this.WorkOrderCloseDate = null;
		this.ResponsibleUserNumber = workOrder.getResponsible().getPerNumber().toString();
		this.ReleasedUserNumber = workOrder.getResponsible().getPerNumber().toString();
		this.CropId = workOrder.getCrop().getErpId().toString();
		this.OperationId = workOrder.getOperation().getErpId().toString();
		this.NoMaterial = "";
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
			mat.setSpentQuantity("0.0");
			mat.setWorkOrderMaterialNumber("");
			
			this.WorkOrderToMaterialNavigation.getResults().add(mat);	
		}
		
		for(WorkOrderWorker wow : workOrder.getWorkers()) {
			WorkOrderEmployeeSAP woeSAP = new WorkOrderEmployeeSAP();
			woeSAP.setWorkOrderEmployeeNumber(wow.getUser().getPerNumber().toString());
			woeSAP.setEmployeeId(wow.getUser().getPerNumber().toString());
			woeSAP.setOperationId(wow.getOperation().getErpId().toString());
			woeSAP.setWorkEffectiveHours("0.00000");
			woeSAP.setWorkSundayHours("0.00000");
			woeSAP.setWorkHolidayHours("0.00000");
			woeSAP.setOvertimeWork("");
			woeSAP.setOperationOutput("0.00000");
			woeSAP.setOperationOutputUnit("");
			woeSAP.setNoOperationOutput("");
			woeSAP.setMasterMachineId(wow.getMachine().getErpId().toString());
			woeSAP.setSlaveMachineId(wow.getConnectingMachine().getErpId().toString());
			woeSAP.setMachineTimeStart("0.0");
			woeSAP.setMachineTimeEnd("0.0");
			woeSAP.setMachineEffectiveHours("0.00000");
			woeSAP.setMachineAreaOutput("0.00000");
			woeSAP.setSpentFuel("0.00000");
			woeSAP.setWorkNightHours("0.00000");
			woeSAP.setMachineTime("0.0");
			woeSAP.setDeleted("");
			this.WorkOrderToEmployeeNavigation.getResults().add(woeSAP);
		}
		
	}

}
