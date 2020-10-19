package org.mkgroup.zaga.workorderservice.service;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.repository.UserRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderWorkerService {
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	OperationRepository operationRepo;
	
	@Autowired
	MachineRepository machineRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	public WorkOrderWorker getOne(UUID id) {
		try {
			WorkOrderWorker wow = wowRepo.getOne(id);
			return wow;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteWow(UUID id) {
		try {
			wowRepo.deleteById(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateWorkOrder(UUID id, WorkOrderWorkerDTO wowDTO) {
		WorkOrderWorker wow = wowRepo.getOne(id);
		if(wowDTO.getDayPeriod() != null) {
			wow.setDayPeriod(wowDTO.getDayPeriod());
		} else {
			wow.setDayPeriod(-1.0);
		}
		if(wowDTO.getNightPeriod() != null) {
			wow.setNightPeriod(wowDTO.getNightPeriod());
		} else {
			wow.setNightPeriod(-1.0);
		}
		if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
			wow.setWorkPeriod(wowDTO.getDayPeriod() + wowDTO.getNightPeriod());
		} else {
			wow.setWorkPeriod(-1.0);
		}
		if(wowDTO.getInitialState() != null) {
			wow.setInitialState(wowDTO.getInitialState());
		} else {
			wow.setInitialState(-1.0);
		}
		if(wowDTO.getFinalState() != null) {
			wow.setFinalState(wowDTO.getFinalState());
		} else {
			wow.setFinalState(-1.0);
		}
		if(wowDTO.getFinalState() != null && wowDTO.getInitialState() != null) {
			wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		} else {
			wow.setSumState(-1.0);
		}
		if(wowDTO.getFuel() != null) {
			wow.setFuel(wowDTO.getFuel());
		} else {
			wow.setFuel(-1.0);
		}
		wow.setUser(userRepo.getOne(wowDTO.getUser().getUserId()));
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		wow.setConnectingMachine(machineRepo.getOne(wowDTO.getConnectingMachine().getId()));
		wow.setMachine(machineRepo.getOne(wowDTO.getMachine().getId()));
		
		wowRepo.save(wow);
	}
	
	public void addWorker(UUID id, WorkOrderWorkerDTO wowDTO) {
		WorkOrder workOrder = workOrderRepo.getOne(id);
		WorkOrderWorker wow = new WorkOrderWorker();
		if(wowDTO.getNightPeriod() != null) {
			wow.setNightPeriod(wowDTO.getNightPeriod());
		} else {
			wow.setNightPeriod(-1.0);
		}
		if(wowDTO.getDayPeriod() != null) {
			wow.setDayPeriod(wowDTO.getDayPeriod());
		} else {
			wow.setDayPeriod(-1.0);
		}
		if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
			wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
		} else {
			wow.setWorkPeriod(-1.0);
		}
		wow.setOperation(operationRepo.getOne(wowDTO.getOperation().getId()));
		wow.setMachine(machineRepo.getOne(wowDTO.getMachine().getId()));
		wow.setConnectingMachine(machineRepo.getOne(wowDTO.getConnectingMachine().getId()));
		if(wowDTO.getInitialState() != null) {
			wow.setInitialState(wowDTO.getInitialState());
		} else {
			wow.setInitialState(-1.0);
		}
		if(wowDTO.getFinalState() != null) {
			wow.setFinalState(wowDTO.getFinalState());
		} else {
			wow.setFinalState(-1.0);
		}
		if(wowDTO.getInitialState() != null && wowDTO.getFinalState() != null) {
			wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
		} else {
			wow.setSumState(-1.0);
		}
		wow.setWorkOrder(workOrder);
		wow.setUser(userRepo.getOne(wowDTO.getUser().getUserId()));

		wowRepo.save(wow);
	}
}
