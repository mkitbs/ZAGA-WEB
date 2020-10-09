package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderWorkerDTO;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.User;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderMachineRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
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
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	@Autowired
	SpentMaterialRepository spentMaterialRepo;
	
	@Autowired
	WorkOrderMachineRepository womRepo;
	
	public void addWorkOrder(WorkOrderDTO workOrderDTO) {
		try {
			log.info("Work order creation started");
			
			WorkOrder workOrder = new WorkOrder();
			
			LocalDate startDate = new LocalDate(
					Integer.parseInt(workOrderDTO.getDate().getYear()),
					Integer.parseInt(workOrderDTO.getDate().getMonth()),
					Integer.parseInt(workOrderDTO.getDate().getDay()));
			Date startDateToAdd = startDate.toDate();
			workOrder.setDate(startDateToAdd);
			
			workOrder.setStatus(WorkOrderStatus.NEW);
			workOrder.setCreationDate(new Date());
			
			Operation operation = operationService.getOne(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			
			Crop crop = cropService.getOne(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			
			User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
			
			workOrder.setResponsible(responsible);
			
			workOrder = workOrderRepo.save(workOrder);
			System.out.println(workOrder.getId());//zbog testiranja
			
			for(WorkOrderWorkerDTO wowDTO : workOrderDTO.getWorkers()) {
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
				if(wowDTO.getFinalState() != null) {
					wow.setFinalState(wowDTO.getFinalState());
				} else {
					wow.setFinalState(-1.0);
				}
				if(wowDTO.getFuel() != null) {
					wow.setFuel(wowDTO.getFuel());
				} else {
					wow.setFuel(-1.0);
				}
				if(wowDTO.getInitialState() != null) {
					wow.setInitialState(wowDTO.getInitialState());
				} else {
					wow.setInitialState(-1.0);
				}
				if(wowDTO.getInitialState() != null && wowDTO.getFinalState() != null) {
					wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
				} else {
					wow.setSumState(-1.0);
				}
				wow.setWorkOrder(workOrder);
				if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
					wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
				} else {
					wow.setWorkPeriod(-1.0);
				}
				wow.setUser(employeeService.getOne(wowDTO.getUser().getId()));
				wow.setOperation(operationService.getOne(wowDTO.getOperation().getId()));
				wow.setMachine(machineService.getOne(wowDTO.getMachine().getId()));
				
				if(wowDTO.getConnectingMachine().getId() != null) {
					wow.setConnectingMachine(machineService.getOne(wowDTO.getConnectingMachine().getId()));
				}
				wowRepo.save(wow);
			}
		
			for(SpentMaterialDTO m : workOrderDTO.getMaterials()) {
				SpentMaterial material = new SpentMaterial();

				material.setMaterial(materialService.getOne(m.getMaterial().getId()));
				material.setQuantity(m.getQuantity());
				material.setQuantityPerHectar(m.getQuantity() / workOrder.getCrop().getArea());
				if(m.getSpent() != null) {
					material.setSpent(m.getSpent());
					material.setSpentPerHectar(m.getSpent() / workOrder.getCrop().getArea());
				} else {
					material.setSpent(-1.0);
					material.setSpentPerHectar(-1.0);
				}
				material.setWorkOrder(workOrder);
				spentMaterialRepo.save(material);
			}
			
			log.info("Insert work order into db");
			
		}catch(Exception e) {
			log.error("Insert work order faild", e);
		}
	}
	
	public List<WorkOrderDTO> getAll(){
		List<WorkOrder> workOrders = workOrderRepo.findAllOrderByCreationDate();
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
			
			LocalDate startDate = new LocalDate(
					Integer.parseInt(workOrderDTO.getDate().getYear()),
					Integer.parseInt(workOrderDTO.getDate().getMonth()),
					Integer.parseInt(workOrderDTO.getDate().getDay()));
			Date startDateToAdd = startDate.toDate();
			workOrder.setDate(startDateToAdd);
			
			if(workOrderDTO.getStatus().equals("Novi")) {
				workOrder.setStatus(WorkOrderStatus.NEW);
			} else if(workOrderDTO.getStatus().equals("U radu")) {
				workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
			} else {
				workOrder.setStatus(WorkOrderStatus.CLOSED);
			}
			
			workOrder.setCreationDate(workOrderDTO.getCreationDate());
			
			Operation operation = operationService.getOne(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			
			Crop crop = cropService.getOne(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			
			User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
			
			workOrder.setResponsible(responsible);
			
			workOrder = workOrderRepo.save(workOrder);
			System.out.println(workOrder.getId());//zbog testiranja
			
			for(WorkOrderWorkerDTO wowDTO : workOrderDTO.getWorkers()) {
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
				if(wowDTO.getFinalState() != null) {
					wow.setFinalState(wowDTO.getFinalState());
				} else {
					wow.setFinalState(-1.0);
				}
				if(wowDTO.getFuel() != null) {
					wow.setFuel(wowDTO.getFuel());
				} else {
					wow.setFuel(-1.0);
				}
				if(wowDTO.getInitialState() != null) {
					wow.setInitialState(wowDTO.getInitialState());
				} else {
					wow.setInitialState(-1.0);
				}
				if(wowDTO.getInitialState() != null && wowDTO.getFinalState() != null) {
					wow.setSumState(wowDTO.getFinalState() - wowDTO.getInitialState());
				} else {
					wow.setSumState(-1.0);
				}
				wow.setWorkOrder(workOrder);
				if(wowDTO.getDayPeriod() != null && wowDTO.getNightPeriod() != null) {
					wow.setWorkPeriod(wowDTO.getNightPeriod() + wowDTO.getDayPeriod());
				} else {
					wow.setWorkPeriod(-1.0);
				}
				wow.setUser(employeeService.getOne(wowDTO.getUser().getId()));
				wow.setOperation(operationService.getOne(wowDTO.getOperation().getId()));
				wow.setMachine(machineService.getOne(wowDTO.getMachine().getId()));
				
				if(wowDTO.getConnectingMachine().getId() != null) {
					wow.setConnectingMachine(machineService.getOne(wowDTO.getConnectingMachine().getId()));
				}
				wowRepo.save(wow);	
			}
		}catch(Exception e) {
			log.error("Update work order faild", e);
		}
	}
	
	public WorkOrder createCopy(WorkOrder workOrder, Date date) {
		WorkOrder copy = new WorkOrder();
		List<WorkOrderWorker> workers = workOrder.getWorkers();
		List<SpentMaterial> materials = workOrder.getMaterials();
		copy.setCreationDate(new Date());
		copy.setResponsible(workOrder.getResponsible());
		copy.setCrop(workOrder.getCrop());
		copy.setOperation(workOrder.getOperation());
		copy.setTreated(0);
		copy.setClosed(false);
		copy.setWorkers(new ArrayList<WorkOrderWorker>());
		copy.setMaterials(new ArrayList<SpentMaterial>());
		copy.setDate(date);
		copy.setStatus(WorkOrderStatus.IN_PROGRESS);
		copy = workOrderRepo.save(copy);
		System.out.println(copy.getId());
		
		for(WorkOrderWorker worker : workers) {
			WorkOrderWorker wow = new WorkOrderWorker();
			wow.setWorkOrder(copy);
			wow.setMachine(worker.getMachine());
			wow.setUser(worker.getUser());
			wow.setOperation(worker.getOperation());
			wow.setConnectingMachine(worker.getConnectingMachine());
			
			wowRepo.save(wow);
		}
		
		for(SpentMaterial material:materials) {
			SpentMaterial spentMaterial = new SpentMaterial();
			spentMaterial.setMaterial(material.getMaterial());
			spentMaterial.setWorkOrder(copy);
			
			spentMaterialRepo.save(spentMaterial);
		}
		return copy;
	}
	
	public void closeWorkOrder(WorkOrderDTO workOrderDTO) {
		try {
			WorkOrder workOrder = workOrderRepo.getOne(workOrderDTO.getId());
			//this.updateWorkOrder(workOrderDTO);
			workOrder.setTreated(workOrderDTO.getTreated());
			workOrder.setStatus(WorkOrderStatus.CLOSED);
			workOrder.setClosed(true);
			workOrderRepo.save(workOrder);
		} catch(Exception e) {
			log.error("Update work order faild", e);
		}
	}
	
	public List<WorkOrderDTO> getAllByStatus(WorkOrderStatus status){
		List<WorkOrder> workOrders = workOrderRepo.findAllByStatus(status);
		List<WorkOrderDTO> workOrdersDTO = new ArrayList<WorkOrderDTO>();
		for(WorkOrder workOrder : workOrders) {
			WorkOrderDTO workOrderDTO = new WorkOrderDTO(workOrder);
			workOrdersDTO.add(workOrderDTO);
		}
		return workOrdersDTO;
	}
	
}