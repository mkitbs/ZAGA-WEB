package org.mkgroup.zaga.workorderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="sap-gateway", 
			url="https://sapgw.mk-group.org:42080/sap/opu/odata/sap/ZAG2_SRV/", 
			contextId = "sap-gateway",
			fallback = SAPGatewayFallback.class)
public interface SAPGatewayProxy {
	
	@GetMapping(value = "/CultureGroupSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchCultureGroups(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/CultureSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchCultures(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/OperatinGroupSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchOperationGroups(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/OperationSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchOperations(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);

}
