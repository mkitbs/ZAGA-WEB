package org.mkgroup.zaga.workorderservice.feign;

import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="sap4hana", 
url="http://s4hdev.mk-group.org:8010/sap/opu/odata/sap/ZAG2_SRV/", 
contextId = "sap4hana",
fallback = SAP4HanaFallback.class)
public interface SAP4HanaProxy {

	@GetMapping(value = "/WorkOrderSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCSRFToken(
			@RequestHeader("Authorization") String token,
			@RequestHeader("X-CSRF-Token") String csrfToken);
	
	@PostMapping(value = "/WorkOrderSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> sendWorkOrder(
			@RequestHeader("Authorization") String token,
			@RequestHeader("X-CSRF-Token") String csrfToken,
			@RequestBody WorkOrderToSAP workOrder);
}
