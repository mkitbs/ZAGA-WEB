package org.mkgroup.zaga.workorderservice.dtoSAP;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderToEmployeeNavigation {

	private List<WorkOrderEmployeeSAP> results = new ArrayList<WorkOrderEmployeeSAP>();
}
