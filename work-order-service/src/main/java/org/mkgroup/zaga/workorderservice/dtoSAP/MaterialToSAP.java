package org.mkgroup.zaga.workorderservice.dtoSAP;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialToSAP {

	@JsonProperty("WorkOrderMaterialNumber")
	private String WorkOrderMaterialNumber;
	@JsonProperty("MaterialId")
	private String MaterialId;
	@JsonProperty("PlannedQuantity")
	private String PlannedQuantity;
	@JsonProperty("SpentQuantity")
	private String SpentQuantity;
	@JsonProperty("MaterialUnit")
	private String MaterialUnit;
	@JsonProperty("Charge")
	private String Charge;
	@JsonProperty("Deleted")
	private String Deleted;
}
