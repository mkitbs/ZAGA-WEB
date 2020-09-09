package org.mkgroup.zaga.workorderservice.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SAPGatewayFallback implements SAPGatewayProxy {

	@Override
	public ResponseEntity<Object> fetchCultureGroups(String format, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> fetchCultures(String format, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> fetchOperationGroups(String format, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> fetchOperations(String format, String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
