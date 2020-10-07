package org.mkgroup.zaga.workorderservice.dtoSAP;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialToSAP {

	private String WorkOrderMaterialNumber;
	private String MaterialId;
	private String PlannedQuantity;
	private String SpentQuantity;
	private String MaterialUnit;
	private String Charge;
	private String Deleted;
}
