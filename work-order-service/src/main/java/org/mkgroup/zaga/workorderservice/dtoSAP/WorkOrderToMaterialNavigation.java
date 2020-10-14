package org.mkgroup.zaga.workorderservice.dtoSAP;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderToMaterialNavigation {

	private List<MaterialToSAP> results = new ArrayList<MaterialToSAP>();
}
