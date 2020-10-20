package org.mkgroup.zaga.workorderservice.dtoSAP;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkOrderCloseToReturnNavig {

	private List<Object> results = new ArrayList<Object>();
}
