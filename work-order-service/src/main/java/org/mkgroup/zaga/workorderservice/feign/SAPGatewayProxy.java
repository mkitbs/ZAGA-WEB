package org.mkgroup.zaga.workorderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="sap-gateway", 
			url="${sap.services.gateway}", 
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
	
	@GetMapping(value = "/OperationGroupSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchOperationGroups(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/OperationSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchOperations(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/EmployeeSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchEmployees(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/MachineSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchMachines(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);

	@GetMapping(value = "/MaterialSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchMaterials(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);

	@GetMapping(value = "/CropSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchCrops(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/CropVarietySet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchCropVarieties(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/VarietySet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchVarieties(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/FieldGroupSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchFieldGroups(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/FieldSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchFields(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/MachineGroupSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> fetchMachineGroups(
			@RequestParam(name = "$format") String format,
			@RequestHeader("Authorization") String token);
	
	@GetMapping(value = "/WorkOrderSet", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCSRFToken(
			@RequestHeader("Authorization") String token,
			@RequestHeader("X-CSRF-Token") String csrfToken);
}
