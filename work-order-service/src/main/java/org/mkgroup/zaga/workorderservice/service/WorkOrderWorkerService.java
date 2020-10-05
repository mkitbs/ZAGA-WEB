package org.mkgroup.zaga.workorderservice.service;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderWorkerService {
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	public WorkOrderWorker getOne(UUID id) {
		try {
			WorkOrderWorker wow = wowRepo.getOne(id);
			return wow;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
