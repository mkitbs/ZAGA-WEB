package org.mkgroup.zaga.workorderservice.controller;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.mkgroup.zaga.workorderservice.service.WorkOrderWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	MachineRepository machineRepo;

	@PostMapping("addWorker/{id}")
	public ResponseEntity<?> addWorker(@PathVariable UUID id,@RequestBody WorkOrderWorkerDTO wowDTO){
		WorkOrder workOrder = workOrderRepo.getOne(id);
		WorkOrderWorker wow = new WorkOrderWorker();
		wow.setNightPeriod(wowDTO.getNightPeriod());
		wow.setDayPeriod(wowDTO.getDayPeriod());
		wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		wow.setMachine(machineRepo.getOne(wowDTO.getMachine().getId()));
		wow.setConnectingMachine(machineRepo.getOne(wowDTO.getConnectingMachine().getId()));
		wow.setInitialState(wowDTO.getInitialState());
		wow.setFinalState(wowDTO.getFinalState());
		wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		wow.setWorkOrder(workOrder);
		wow.setUser(userRepo.getOne(wowDTO.getUser().getId()));

		wowRepo.save(wow);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("updateWorkOrderWorker/{id}")
	public ResponseEntity<?> updateWorkOrderWorker(@PathVariable UUID id, @RequestBody WorkOrderWorkerDTO wowDTO){
		WorkOrderWorker wow = wowService.getOne(id);
		wow.setDayPeriod(wowDTO.getDayPeriod());
		wow.setNightPeriod(wowDTO.getNightPeriod());
		wow.setWorkPeriod(wowDTO.getDayPeriod() + wowDTO.getNightPeriod());
		wow.setInitialState(wowDTO.getInitialState());
		wow.setFinalState(wowDTO.getFinalState());
		wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		wow.setFuel(wowDTO.getFuel());
		wow.setUser(userRepo.getOne(wowDTO.getUser().getId()));
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		wow.setConnectingMachine(machineRepo.getOne(wowDTO.getConnectingMachine().getId()));
		wow.setMachine(machineRepo.getOne(wowDTO.getMachine().getId()));
		
		wowRepo.save(wow);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
