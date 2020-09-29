package org.mkgroup.zaga.workorderservice.controller;

import java.util.Date;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.WorkOrderMachineDTO;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderMachine;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderMachineRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workOrderMachine/")
public class WorkOrderMachineController {

	@Autowired
	WorkOrderMachineRepository womRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	MachineRepository machineRepo;
	
	@PostMapping("{id}")
	public ResponseEntity<?> addMachine(@PathVariable UUID id, @RequestBody WorkOrderMachineDTO womDTO){
		WorkOrder workOrder = workOrderRepo.getOne(id);
		WorkOrderMachine wom = new WorkOrderMachine();
		wom.setWorkOrder(workOrder);
		wom.setDate(new Date());//zakucano
		wom.setInitialState(womDTO.getFinalState());
		wom.setFinalState(womDTO.getFinalState());
		wom.setSumState(womDTO.getSumState());
		wom.setWorkPeriod(wom.getWorkPeriod());
		
		User user = userRepo.getOne(womDTO.getUser().getId());
		wom.setUser(user);
		
		Machine machine = machineRepo.getOne(womDTO.getMachine().getId());
		wom.setMachine(machine);
		
		womRepo.save(wom);
		return new ResponseEntity<WorkOrderMachine>(wom, HttpStatus.OK);
	}
	
}
