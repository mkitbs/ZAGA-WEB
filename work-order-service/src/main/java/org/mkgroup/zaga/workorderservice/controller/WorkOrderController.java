package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mkgroup.zaga.workorderservice.dto.ATMReportResponseDTO;
import org.mkgroup.zaga.workorderservice.dto.AllWorkOrdersResponseDTO;
import org.mkgroup.zaga.workorderservice.dto.DateDTO;
import org.mkgroup.zaga.workorderservice.dto.OperationsTodayDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.CloseWorkOrderResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.SAPResponse;
import org.mkgroup.zaga.workorderservice.dtoSAP.WorkOrderToSAP;
import org.mkgroup.zaga.workorderservice.feign.SAP4HanaProxy;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/workOrder")
public class WorkOrderController {
	
	@Autowired
	WorkOrderService workOrderService;
	
	@Autowired
	WorkOrderRepository wrepo;
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAP4HanaProxy sap4hana;
	
	@PostMapping("/createWorkOrder")
	public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrderDTO request, @RequestHeader("SapUserId") String sapuserid, @RequestHeader("TenantId") String tenantId) throws Exception{
		//try {
			SAPResponse sapResponse = workOrderService.addWorkOrder(request, sapuserid, Long.parseLong(tenantId));
			
			if(sapResponse.isSuccess()) {
				return new ResponseEntity<SAPResponse>(sapResponse ,HttpStatus.OK);
			}else {
				return new ResponseEntity<SAPResponse>(sapResponse, HttpStatus.BAD_REQUEST);
			}
		//}catch(Exception e) {
		//	return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		//}
	}
	
	@PostMapping("/createCopy/{id}")
	public ResponseEntity<?> copyWorkOrder(@PathVariable UUID id, @RequestBody DateDTO date, @RequestHeader("SapUserId") String sapuserid, @RequestHeader("TenantId") String tenantId){
		WorkOrder workOrder = wrepo.getOne(id);
	
		WorkOrder copy;
		try {
			copy = workOrderService.createCopy(workOrder, date, sapuserid, Long.parseLong(tenantId));
			return new ResponseEntity<UUID>(copy.getId(), HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	@PostMapping("/createTestWorkOrder")
	public ResponseEntity<?> testWO(@RequestBody String t) {
		WorkOrder wo = new WorkOrder();
		wo = wrepo.save(wo);
		//returning as expected, uuid works
		return new ResponseEntity<WorkOrder>(wo, HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllWorkOrders(@RequestHeader("TenantId") String tenantId){
		List<AllWorkOrdersResponseDTO> workOrders = workOrderService.getAll(Long.parseLong(tenantId));
		return new ResponseEntity<List<AllWorkOrdersResponseDTO>>(workOrders, HttpStatus.OK);
	}
	
	@GetMapping("/getWorkOrder/{id}")
	public ResponseEntity<?> getWorkOrder(@PathVariable UUID id){
		WorkOrderDTO workOrder = workOrderService.getOne(id);
		if(workOrder != null) {
			return new ResponseEntity<WorkOrderDTO>(workOrder, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/updateWorkOrder")
	public ResponseEntity<?> updateWorkOrder(@RequestBody WorkOrderDTO request){
		try {
			SAPResponse sapResponse = workOrderService.updateWorkOrder(request);
			if(sapResponse.isSuccess()) {
				return new ResponseEntity<SAPResponse>(sapResponse, HttpStatus.OK);
			}else {
				return new ResponseEntity<SAPResponse>(sapResponse, HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception e) {
			return new ResponseEntity<String>("Work order not updated. Error " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/closeWorkOrder")
	public ResponseEntity<?> closeWorkOrder(@RequestBody WorkOrderDTO request) throws Exception{
		//try {
			CloseWorkOrderResponse closeWO =  workOrderService.closeWorkOrder(request);
			if(closeWO.isStatus()) {
				return new ResponseEntity<>(HttpStatus.OK);
			}else {
				return new ResponseEntity<List<String>>(closeWO.getErrors(),HttpStatus.BAD_REQUEST);
			}
			
		//} catch (Exception e) {
		//	System.out.println(e.getMessage());
		//	return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		//}
	}
	
	@GetMapping("/getAllByStatus/{status}")
	public ResponseEntity<?> getAllByStatus(@PathVariable WorkOrderStatus status, @RequestHeader("TenantId") String tenantId){
		List<AllWorkOrdersResponseDTO> workOrders = workOrderService.getAllByStatus(Long.valueOf(tenantId), status);
		return new ResponseEntity<List<AllWorkOrdersResponseDTO>>(workOrders, HttpStatus.OK);
	}
	
	@GetMapping("/getMyWorkOrders")
	public ResponseEntity<?> getMyWorkOrders(@RequestHeader("SapUserId") String sapUserId, @RequestHeader("TenantId") String tenantId){
		List<AllWorkOrdersResponseDTO> workOrders = workOrderService.getMyWorkOrders(Long.valueOf(tenantId), sapUserId);
		return new ResponseEntity<List<AllWorkOrdersResponseDTO>>(workOrders, HttpStatus.OK);
	}
	
	@GetMapping("/getMyWoByStatus/{status}")
	public ResponseEntity<?> getMyWoByStatus(@PathVariable WorkOrderStatus status, @RequestHeader("SapUserId") String sapUserId, @RequestHeader("TenantId") String tenantId){
		List<AllWorkOrdersResponseDTO> workOrders = workOrderService.getMyWoByStatus(Long.parseLong(sapUserId), Long.parseLong(tenantId), status);
		return new ResponseEntity<List<AllWorkOrdersResponseDTO>>(workOrders, HttpStatus.OK);
	}
	
	@GetMapping("/getOperationsForToday")
	public ResponseEntity<?> getOperationsForToday(@RequestHeader("TenantId") String tenantId) { 
		List<OperationsTodayDTO> retVals = workOrderService.getOperationsForToday(Long.parseLong(tenantId));
		return new ResponseEntity<List<OperationsTodayDTO>>(retVals, HttpStatus.OK);
	}
	
	@GetMapping("/sync")
	public ResponseEntity<?> synchronization(){
		workOrderService.synch();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/getDataForATMReport")
	public ResponseEntity<?> getDataForATMReport(@RequestHeader("TenantId") String tenantId) {
		List<ATMReportResponseDTO> retVals = workOrderService.getDataForATMReport(Long.parseLong(tenantId));
		return new ResponseEntity<List<ATMReportResponseDTO>>(retVals, HttpStatus.OK);
	}
	
	@GetMapping("/syncNoOperationOutput")
	public ResponseEntity<?> syncNoOperationOutput() {
		workOrderService.syncNoOperationOutput();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/syncOperationOutput")
	public ResponseEntity<?> syncOperationOutput() {
		workOrderService.syncOperationOutput();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/updateSyncWorkOrder/{id}")
	public ResponseEntity<?> updateSyncWorkOrder(@RequestBody Object json, @PathVariable UUID id) {
		System.out.println("AAAAAAAAAA");
		Gson gson = new Gson();
		JsonObject json2 = gson.fromJson(json.toString(), JsonObject.class);
		workOrderService.updateSync(json2, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/syncCancellation")
	public ResponseEntity<?> syncCancellation(@RequestBody List<Long> ids) {
		workOrderService.syncCancellation(ids);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
