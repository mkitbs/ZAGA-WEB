package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.dto.MachineStateDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerDTO;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.MachineState;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.model.Worker;
import org.mkgroup.zaga.workorderservice.model.WorkerHours;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderService {
	
	private static final Logger log = Logger.getLogger(WorkOrderService.class);
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	WorkerService workerService;
	
	@Autowired 
	OperationService operationService;
	
	@Autowired
	CropService cropService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	WorkerHoursService workerHoursService;
	
	@Autowired
	MachineService machineService;
	
	@Autowired
	MaterialService materialService;
	
	@Autowired
	MachineStateService machineStateService;
	
	@Autowired
	SpentMaterialService spentMaterialService;
	
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
			
			User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
			workOrder.setResponsible(responsible);
			
			List<Worker> workers = new ArrayList<Worker>();
			for(WorkerDTO w : workOrderDTO.getWorkers()) {
				Worker worker = new Worker();
				worker.setUserId(w.getUserId());
				worker.getWorkOrders().add(workOrder);
				worker = workerService.addWorker(worker);
				workers.add(worker);
				
				WorkerHours wh = new WorkerHours();
				wh.setOperationId(w.getOperationId());
				wh.setWorkerId(worker.getId());
				wh = workerHoursService.addWorkerHours(wh);
			}
			workOrder.setWorkers(workers);
			
			List<Machine> machines = new ArrayList<Machine>();
			for(MachineStateDTO m : workOrderDTO.getMachines()) {
				Machine machine = machineService.getOne(m.getMachineId());
				machines.add(machine);
				
				MachineState ms = new MachineState();
				ms.setMachineId(m.getMachineId());
				for(Worker w : workers) {
					if(w.getUserId().equals(m.getUserId())) {
						ms.setWorkerId(w.getId());
					} else {
						Worker worker = new Worker();
						worker.setUserId(m.getUserId());
						worker.getWorkOrders().add(workOrder);
						worker = workerService.addWorker(worker);
						ms.setWorkerId(worker.getId());
					}
				}
				ms = machineStateService.addMachineState(ms);
			}
			workOrder.setMachines(machines);
			
			List<Material> materials = new ArrayList<Material>();
			for(SpentMaterialDTO m : workOrderDTO.getMaterials()) {
				Material material = materialService.getOne(m.getMaterialId());
				materials.add(material);
				
				SpentMaterial spentMaterial = new SpentMaterial();
				spentMaterial.setMaterialId(m.getMaterialId());
				spentMaterial.setQuantity(m.getQuantity());
				spentMaterial.setQuantityPerHectar(m.getQuantityPerHectar());
				spentMaterial = spentMaterialService.addSpentMaterial(spentMaterial);
			}
			workOrder.setMaterials(materials);
			
			
			
			log.info("Insert work order into db");
			workOrderRepo.save(workOrder);
		}catch(Exception e) {
			log.error("Insert work order faild", e);
		}
	}
	
	public List<WorkOrderDTO> getAll(){
		List<WorkOrder> workOrders = workOrderRepo.findAll();
		List<WorkOrderDTO> workOrdersDTO = new ArrayList<WorkOrderDTO>();
		for(WorkOrder workOrder : workOrders) {
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			workOrdersDTO.add(workOrderDTO);
		}
		return workOrdersDTO;
	}
	
	public WorkOrderDTO getOne(UUID id) {
		try {
			WorkOrder workOrder = workOrderRepo.getOne(id);
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			return workOrderDTO;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void updateWorkOrder(WorkOrderDTO workOrderDTO) {
		try {
			log.info("Work order update started");
			
			WorkOrder workOrder = workOrderRepo.getOne(workOrderDTO.getId());
			
			workOrder.setStartDate(workOrderDTO.getStart());
			workOrder.setEndDate(workOrderDTO.getEnd());
			if(workOrderDTO.getStatus().equalsIgnoreCase("Novi"))
				workOrder.setStatus(WorkOrderStatus.NEW);
			else if(workOrderDTO.getStatus().equalsIgnoreCase("U radu"))
				workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
			else if(workOrderDTO.getStatus().equalsIgnoreCase("Zatvoren"))
				workOrder.setStatus(WorkOrderStatus.CLOSED);
			
			Operation operation = operationService.getOne(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			
			Crop crop = cropService.getOne(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			
			User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
			workOrder.setResponsible(responsible);
			
			List<Worker> workers = new ArrayList<Worker>();
			for(WorkerDTO w : workOrderDTO.getWorkers()) {
				Worker worker = new Worker();
				worker.setUserId(w.getUserId());
				worker.getWorkOrders().add(workOrder);
				worker = workerService.addWorker(worker);
				workers.add(worker);
				
				WorkerHours wh = new WorkerHours();
				wh.setOperationId(w.getOperationId());
				wh.setWorkerId(worker.getId());
				wh = workerHoursService.addWorkerHours(wh);
			}
			workOrder.setWorkers(workers);
			
			List<Machine> machines = new ArrayList<Machine>();
			for(MachineStateDTO m : workOrderDTO.getMachines()) {
				Machine machine = machineService.getOne(m.getMachineId());
				machines.add(machine);
				
				MachineState ms = new MachineState();
				ms.setMachineId(m.getMachineId());
				for(Worker w : workers) {
					if(w.getUserId().equals(m.getUserId())) {
						ms.setWorkerId(w.getId());
					} else {
						Worker worker = new Worker();
						worker.setUserId(m.getUserId());
						worker.getWorkOrders().add(workOrder);
						worker = workerService.addWorker(worker);
						ms.setWorkerId(worker.getId());
					}
				}
				ms = machineStateService.addMachineState(ms);
			}
			workOrder.setMachines(machines);
			
			List<Material> materials = new ArrayList<Material>();
			for(SpentMaterialDTO m : workOrderDTO.getMaterials()) {
				Material material = materialService.getOne(m.getMaterialId());
				materials.add(material);
				
				SpentMaterial spentMaterial = new SpentMaterial();
				spentMaterial.setMaterialId(m.getMaterialId());
				spentMaterial.setQuantity(m.getQuantity());
				spentMaterial.setQuantityPerHectar(m.getQuantityPerHectar());
				spentMaterial = spentMaterialService.addSpentMaterial(spentMaterial);
			}
			workOrder.setMaterials(materials);
			
			log.info("Update a work order in the db");
			workOrderRepo.save(workOrder);
		}catch(Exception e) {
			log.error("Update work order faild", e);
		}
	}
	
}
