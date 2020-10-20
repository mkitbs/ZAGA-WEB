package org.mkgroup.zaga.workorderservice.feign;

import org.mkgroup.zaga.workorderservice.dtoSAP.CloseWorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SAP4HanaFallback implements SAP4HanaProxy {

	@Override
	public ResponseEntity<Object> getCSRFToken(String token, String csrfToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> sendWorkOrder(String cookie, String token, String csrfToken, String xmlRequestWith,
			WorkOrderToSAP workOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> getCSRFTokenClose(String token, String csrfToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> closeWorkOrder(String cookie, String token, String csrfToken, String xmlRequestWith,
			CloseWorkOrderDTO closeWorkOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
