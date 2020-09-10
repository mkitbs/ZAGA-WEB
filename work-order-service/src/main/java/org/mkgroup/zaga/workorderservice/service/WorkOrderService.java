package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderDTO;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.model.WorkOrderStatus;
import org.mkgroup.zaga.workorderservice.repository.CropRepository;
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
	CropRepository cropRepo;
	
	public void addWorkOrder(WorkOrderDTO workOrderDTO) {
		try {
			log.info("Insert work order into db");
			WorkOrder workOrder = new WorkOrder();
			workOrder.setStartDate(workOrderDTO.getStart());
			workOrder.setEndDate(workOrderDTO.getEnd());
			workOrder.setStatus(WorkOrderStatus.NEW);
			Operation operation = operationRepo.findById(workOrderDTO.getOperationId());
			workOrder.setOperation(operation);
			Crop crop = cropRepo.findById(workOrderDTO.getCropId());
			workOrder.setCrop(crop);
			List<Machine> machines = new ArrayList<Machine>();
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.map(workOrderDTO.getMachines(), machines);
			workOrder.setMachines(machines);
			List<Material> materials = new ArrayList<Material>();
			modelMapper.map(workOrderDTO.getMaterials(), materials);
			workOrder.setMaterials(materials);
			workOrderRepo.save(workOrder);
		}catch(Exception e) {
			log.error("Insert work order faild", e);
		}
	}
}
