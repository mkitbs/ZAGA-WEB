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
				
				wow.setDayNightPeriod(wowDTO.getDayNightPeriod());
				wow.setDayWorkPeriod(wowDTO.getDayPeriod());
				wow.setFinalState(wowDTO.getFinalState());
				wow.setFuel(wowDTO.getFuel());
				wow.setInitialState(wowDTO.getInitialState());
				wow.setSumState(wowDTO.getSumState());
				wow.setWorkOrder(workOrder);
				wow.setWorkPeriod(wowDTO.getWorkPeriod());
				wow.setUser(employeeService.getOne(wowDTO.getUser().getId()));
				wow.setOperation(operationService.getOne(wowDTO.getOperation().getId()));
				wow.setMachine(machineService.getOne(wowDTO.getMachine().getId()));
				
				if(wowDTO.getConnectingMachine().getId() != null) {
					wow.setConnectingMachine(machineService.getOne(wowDTO.getConnectingMachine().getId()));
				}
				wowRepo.save(wow);
			}
			
			/*for(WorkOrderMachineDTO m : workOrderDTO.getMachines()) {
				WorkOrderMachine wom = new WorkOrderMachine();
				
				wom.setDate(new Date());
				wom.setFinalState(0);
				wom.setInitialState(0);
				wom.setMachine(machineService.getOne(m.getMachine().getId()));
				wom.setUser(employeeService.getOne(m.getUser().getId()));
				wom.setWorkPeriod(0);
				wom.setWorkOrder(workOrder);
				womRepo.save(wom);
			}*/
		
			for(SpentMaterialDTO m : workOrderDTO.getMaterials()) {
				SpentMaterial material = new SpentMaterial();

				material.setMaterial(materialService.getOne(m.getMaterial().getId()));
				material.setQuantity(m.getQuantity());
				material.setQuantityPerHectar(m.getQuantityPerHectar());
				material.setSpent(m.getSpent());
				material.setSpentPerHectar(m.getSpentPerHectar());
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
			
			//workOrder.setStartDate(workOrderDTO.getStart());
			//workOrder.setEndDate(workOrderDTO.getEnd());
			workOrder.setStatus(WorkOrderStatus.NEW);
			
			Operation operation = operationService.getOne(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			
			Crop crop = cropService.getOne(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			
			User responsible = employeeService.getOne(workOrderDTO.getResponsibleId());
			
			workOrder.setResponsible(responsible);
			
			
			workOrder = workOrderRepo.save(workOrder);
			System.out.println(workOrder.getId());//zbog testiranja
			
			/*for(WorkOrderMachineDTO m : workOrderDTO.getMachines()) {
				WorkOrderMachine wom = new WorkOrderMachine();
				
				wom.setDate(new Date());
				wom.setFinalState(0);
				wom.setInitialState(0);
				wom.setMachine(machineService.getOne(m.getMachine().getId()));
				wom.setUser(employeeService.getOne(m.getUser().getId()));
				wom.setWorkPeriod(0);
				wom.setWorkOrder(workOrder);
				womRepo.save(wom);
			}*/
		
			for(SpentMaterialDTO m : workOrderDTO.getMaterials()) {
				SpentMaterial material = new SpentMaterial();

				material.setMaterial(materialService.getOne(m.getMaterial().getId()));
				material.setQuantity(m.getQuantity());
				material.setQuantityPerHectar(m.getQuantityPerHectar());
				material.setSpent(0);
				material.setSpentPerHectar(0);
				material.setWorkOrder(workOrder);
				spentMaterialRepo.save(material);
			}
			
			log.info("Update a work order in the db");
		}catch(Exception e) {
			log.error("Update work order faild", e);
		}
	}
	
}