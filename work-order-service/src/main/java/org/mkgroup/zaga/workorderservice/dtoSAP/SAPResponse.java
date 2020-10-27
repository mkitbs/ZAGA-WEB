package org.mkgroup.zaga.workorderservice.dtoSAP;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SAPResponse {

	private Long erpId;
	private List<String> message = new ArrayList<String>();
	private boolean success;
}
