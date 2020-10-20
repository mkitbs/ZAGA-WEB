package org.mkgroup.zaga.workorderservice.dtoSAP;

import java.util.UUID;

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
	@JsonProperty("WebBackendId")
	private UUID WebBackendId;
}
