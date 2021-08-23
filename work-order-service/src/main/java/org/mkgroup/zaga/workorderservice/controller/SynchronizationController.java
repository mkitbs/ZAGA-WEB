package org.mkgroup.zaga.workorderservice.controller;

import org.mkgroup.zaga.workorderservice.service.CropService;
import org.mkgroup.zaga.workorderservice.service.CropVarietyService;
import org.mkgroup.zaga.workorderservice.service.CultureGroupService;
import org.mkgroup.zaga.workorderservice.service.CultureService;
import org.mkgroup.zaga.workorderservice.service.EmployeeService;
import org.mkgroup.zaga.workorderservice.service.FieldGroupService;
import org.mkgroup.zaga.workorderservice.service.FieldService;
import org.mkgroup.zaga.workorderservice.service.MachineGroupService;
import org.mkgroup.zaga.workorderservice.service.MachineService;
import org.mkgroup.zaga.workorderservice.service.MaterialService;
import org.mkgroup.zaga.workorderservice.service.OperationGroupService;
import org.mkgroup.zaga.workorderservice.service.OperationService;
import org.mkgroup.zaga.workorderservice.service.VarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync/")
public class SynchronizationController {
	
	@Autowired
	MaterialService materialService;

	@Autowired
	MachineGroupService machineGroupService;
	
	@Autowired
	MachineService machineService;
	
	@Autowired
	FieldGroupService fieldGroupService;
	
	@Autowired
	FieldService fieldService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	CultureGroupService cultureGroupService;
	
	@Autowired
	CultureService cultureService;
	
	@Autowired
	CropVarietyService cropVarietyService;
	
	@Autowired
	CropService cropService;
	
	@Autowired
	OperationGroupService operationGroupService;
	
	@Autowired
	OperationService operationService;
	
	@Autowired
	VarietyService varietyService;
	
	@GetMapping
	public ResponseEntity<?> syncData(@RequestParam String type){
		if(type.equals("material")) {
			materialService.getMaterialsFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("machine_group")){
			machineGroupService.getMachineGroupsFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("machine")){
			machineService.getMachinesFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("field_group")){
			fieldGroupService.getFieldGroupsFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("field")){
			fieldService.getFieldsFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("employee")){
			employeeService.getEmployeesFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("culture_group")){
			cultureGroupService.getCultureGroupsFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("culture")){
			cultureService.getCulturesFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("variety")){
			varietyService.getVarietiesFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("crop")){
			cropService.getCropsFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("operation_group")){
			operationGroupService.getOperationGroupsFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else if(type.equals("operation")){
			operationService.getOperationsFromSAP();
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
