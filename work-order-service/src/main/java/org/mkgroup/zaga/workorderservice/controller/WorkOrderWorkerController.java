package org.mkgroup.zaga.workorderservice.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Path;

import org.mkgroup.zaga.workorderservice.dto.MachineSumFuelPerCultureReportDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumStatePerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineSumStatePerCultureReportDTO;
import org.mkgroup.zaga.workorderservice.dto.NumOfEmployeesPerOperationDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentHourOfWorkerPerCultureReportDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderTractorDriverDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerReportDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.SAPResponse;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.mkgroup.zaga.workorderservice.service.WorkOrderService;
import org.mkgroup.zaga.workorderservice.service.WorkOrderWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workOrderWorker/")
public class WorkOrderWorkerController {
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	OperationRepository operationRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	WorkOrderWorkerService wowService;
	
	@Autowired
	WorkOrderService workOrderService;
	
	@Autowired
	MachineRepository machineRepo;

	@PostMapping("addWorker/{id}")
	public ResponseEntity<?> addWorker(@PathVariable UUID id,@RequestBody WorkOrderWorkerDTO wowDTO) throws Exception{
			wowService.addWorker(id, wowDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		
		
	}
	
	@PostMapping("updateWorkOrderWorker/{id}")
	public ResponseEntity<?> updateWorkOrderWorker(@PathVariable UUID id, @RequestBody WorkOrderWorkerDTO wowDTO) throws Exception{
		SAPResponse response = wowService.updateWorkOrder(id, wowDTO);
		if(response.isSuccess()) {
			return new ResponseEntity<SAPResponse>(response, HttpStatus.OK);
		}else {
			return new ResponseEntity<SAPResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("deleteWorkOrderWorker/{id}")
	public ResponseEntity<?> deleteWow(@PathVariable UUID id){
		wowService.deleteWow(id);
		WorkOrderWorker wow = wowRepo.getOne(id);
		try {
			SAPResponse response = workOrderService.updateDataWorkOrder(wow.getWorkOrder());
			return new ResponseEntity<SAPResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SAPResponse response = new SAPResponse();
			response.setSuccess(false);
			response.getMessage().add(e.getMessage());
			return new ResponseEntity<SAPResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("updateWorkOrderWorkerBasicInfo/{id}")
	public ResponseEntity<?> updateWorkOrderWorkerBasicInfo(@PathVariable UUID id, @RequestBody WorkOrderWorkerDTO wowDTO) throws Exception{
		//try {
			wowService.updateWOWBasicInfo(id, wowDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		//} catch (Exception e) {
			// TODO Auto-generated catch block
		//	return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		//}
	}
	
	@GetMapping("getDataForReport")
	public ResponseEntity<?> getDataForReport(@RequestHeader("TenantId") String tenantId){
		List<WorkerReportDTO> data = wowService.getWorkersForReport(Long.parseLong(tenantId));
		return new ResponseEntity<List<WorkerReportDTO>>(data, HttpStatus.OK);
	}
	
	@GetMapping("getWorkOrdersForTractorDriver")
	public ResponseEntity<?> getWorkOrdersForTractorDriver(@RequestHeader("SapUserId") String sapUserId, @RequestHeader("TenantId") String tenantId){
		System.out.println("SAP ID ->" + sapUserId);
		User user = userRepo.findByPerNumber(Long.parseLong(sapUserId)).get();
		List<WorkOrderTractorDriverDTO> data = wowService.getWorkOrdersForTractorDriver(user.getId(), Long.parseLong(tenantId));
		return new ResponseEntity<List<WorkOrderTractorDriverDTO>>(data, HttpStatus.OK);
	}
	
	@GetMapping("getOne/{id}")
	public ResponseEntity<?> getOne(@PathVariable UUID id){
		WorkOrderWorker wow = wowService.getOne(id);
		WorkOrderWorkerDTO wowDTO = new WorkOrderWorkerDTO(wow);
		return new ResponseEntity<WorkOrderWorkerDTO>(wowDTO, HttpStatus.OK);
	}
	
	@GetMapping("getNumOfEmployeesPerOperation")
	public ResponseEntity<?> getNumOfEmployeesPerOperation(@RequestHeader("TenantId") String tenantId) { 
		List<NumOfEmployeesPerOperationDTO> retVals = wowService.getWorkersPerOperation(Long.parseLong(tenantId));
		return new ResponseEntity<List<NumOfEmployeesPerOperationDTO>>(retVals, HttpStatus.OK);
	}
	
	@GetMapping("getHourOfWorkerPerCulture")
	public ResponseEntity<?> getHourOfWorkerPerCulture(@RequestHeader("TenantId") String tenantId){
		List<SpentHourOfWorkerPerCultureReportDTO> retVals = wowService.getHourOfWorkerPerCulture(Long.parseLong(tenantId));
		return new ResponseEntity<List<SpentHourOfWorkerPerCultureReportDTO>>(retVals, HttpStatus.OK);
	}
	
	@GetMapping("getMachineSumStatePerCulture")
	public ResponseEntity<?> getMachineSumStatePerCulture(@RequestHeader("TenantId") String tenantId){
		List<MachineSumStatePerCultureReportDTO> retVals = wowService.getMachineSumStatePerCulture(Long.parseLong(tenantId));
		return new ResponseEntity<List<MachineSumStatePerCultureReportDTO>>(retVals, HttpStatus.OK);
	}
	
	@GetMapping("getMachineSumState")
	public ResponseEntity<?> getMachineSumState(@RequestHeader("TenantId") String tenantId){
		List<MachineSumStatePerCultureDTO> retVals = wowService.getMachineSumState(Long.parseLong(tenantId));
		return new ResponseEntity<List<MachineSumStatePerCultureDTO>>(retVals, HttpStatus.OK);
	}
	
	
	@GetMapping("getMachineSumFuelPerCulture")
	public ResponseEntity<?> getMachineSumFuelPerCulture(@RequestHeader("TenantId") String tenantId){
		List<MachineSumFuelPerCultureReportDTO> retVals = wowService.getMachineSumFuelPerCulture(Long.parseLong(tenantId));
		return new ResponseEntity<List<MachineSumFuelPerCultureReportDTO>>(retVals, HttpStatus.OK);
	}
	
}
