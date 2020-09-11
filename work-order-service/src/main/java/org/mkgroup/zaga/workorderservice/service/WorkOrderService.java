package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.model.Worker;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderService {
	
	private static final Logger log = Logger.getLogger(WorkOrderService.class);
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	OperationRepository operationRepo;
	
	@Autowired
	WorkerService workerService;
	
	@Autowired 
	OperationService operationService;
	
	@Autowired
	CropService cropService;
	
	public void addWorkOrder(WorkOrderDTO workOrderDTO) {
		try {
			log.info("Work order creation started");
			
			WorkOrder workOrder = new WorkOrder();
			
			workOrder.setStartDate(workOrderDTO.getStart());
			workOrder.setEndDate(workOrderDTO.getEnd());
			workOrder.setStatus(WorkOrderStatus.NEW);
			
			Operation operation = operationService.getOne(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			
			Crop crop = cropService.getOne(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			
			ModelMapper modelMapper = new ModelMapper();
			
			List<Machine> machines = workOrderDTO.getMachines()
					  .stream()
					  .map(machine -> modelMapper.map(machine, Machine.class))
					  .collect(Collectors.toList());
			workOrder.setMachines(machines);
			
			List<Material> materials = workOrderDTO.getMaterials()
					.stream()
					.map(material -> modelMapper.map(material, Material.class))
					.collect(Collectors.toList());
			workOrder.setMaterials(materials);
			
			List<User> users = workOrderDTO.getEmployees()
					.stream()
					.map(user -> modelMapper.map(user, User.class))
					.collect(Collectors.toList());
			List<Worker> workers = new ArrayList<Worker>();
			for(User user : users) {
				Worker worker = new Worker();
				worker.setUserId(user.getId());
				worker.getWorkOrders().add(workOrder);
				worker = workerService.addWorker(worker);
				workers.add(worker);
			}
			workOrder.setWorkers(workers);
			
			log.info("Insert work order into db");
			workOrderRepo.save(workOrder);
		}catch(Exception e) {
			log.error("Insert work order faild", e);
		}
	}
	
	public List<WorkOrderDTO> getAll(){
		ModelMapper modelMapper = new ModelMapper();
		List<WorkOrder> workOrders = workOrderRepo.findAll();
		List<WorkOrderDTO> workOrdersDTO = workOrders
				.stream()
				.map(workOrder -> modelMapper.map(workOrder, WorkOrderDTO.class))
				.collect(Collectors.toList());
		return workOrdersDTO;
	}
	
}
