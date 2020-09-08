package org.mkgroup.zaga.workorderservice.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SAPGatewayFallback implements SAPGatewayProxy {

	@Override
	public ResponseEntity<Object> testCallCultureGroup(String format, String token) {
		System.out.println("Fallback");
		return null;
	}

}
