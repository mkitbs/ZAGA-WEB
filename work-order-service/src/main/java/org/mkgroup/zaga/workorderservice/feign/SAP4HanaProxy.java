	package org.mkgroup.zaga.workorderservice.feign;

import org.mkgroup.zaga.workorderservice.dtoSAP.CloseWorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="sap4hana", 
url="${sap.services.s4h}", 
contextId = "sap4hana",
fallback = SAP4HanaFallback.class)
public interface SAP4HanaProxy {

	@GetMapping(value = "/WorkOrderSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCSRFToken(
			@RequestHeader("Authorization") String token,
			@RequestHeader("X-CSRF-Token") String csrfToken);
	
	@PostMapping(value = "/WorkOrderSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> sendWorkOrder(
			@RequestHeader("Cookie") String cookie,
			@RequestHeader("Authorization") String token,
			@RequestHeader("X-CSRF-Token") String csrfToken,
			@RequestHeader("X-Requested-With") String xmlRequestWith,
			@RequestBody WorkOrderToSAP workOrder) throws Exception;
	
	@GetMapping(value = "/WorkOrderCloseSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCSRFTokenClose(
			@RequestHeader("Authorization") String token,
			@RequestHeader("X-CSRF-Token") String csrfToken);
	
	@PostMapping(value = "/WorkOrderCloseSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> closeWorkOrder(
			@RequestHeader("Cookie") String cookie,
			@RequestHeader("Authorization") String token,
			@RequestHeader("X-CSRF-Token") String csrfToken,
			@RequestHeader("X-Requested-With") String xmlRequestWith,
			@RequestBody CloseWorkOrderDTO closeWorkOrder) throws Exception;
}
