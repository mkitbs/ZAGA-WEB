package org.mkgroup.zaga.workorderservice.dtoSAP;

import org.mkgroup.zaga.workorderservice.model.WorkOrder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CloseWorkOrderDTO {
	
	@JsonProperty("CompanyCode")
	private String CompanyCode;
	
	@JsonProperty("WorkOrderNumber")
	private String WorkOrderNumber;
	
	@JsonProperty("WorkOrderCloseToReturnNavig")
	private WorkOrderCloseToReturnNavig WorkOrderCloseToReturnNavig;
	
	@JsonProperty("Cancellation")
	@JsonInclude(Include.NON_NULL)
	private String Cancellation;
	
	public CloseWorkOrderDTO(WorkOrder workOrder) {
		this.CompanyCode = "1200";
		this.WorkOrderNumber = workOrder.getErpId().toString();
		this.WorkOrderCloseToReturnNavig = new WorkOrderCloseToReturnNavig();
	}
}

