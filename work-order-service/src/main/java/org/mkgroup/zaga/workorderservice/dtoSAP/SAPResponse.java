package org.mkgroup.zaga.workorderservice.dtoSAP;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SAPResponse {

	private Long erpId;
	private String message;
	private boolean success;
}
