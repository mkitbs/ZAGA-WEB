package org.mkgroup.zaga.workorderservice.service;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.model.Worker;
import org.mkgroup.zaga.workorderservice.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {
	
	private static final Logger log = Logger.getLogger(WorkerService.class);
	
	@Autowired
	WorkerRepository workerRepo;
	
	public Worker addWorker(Worker worker) {
		try {
			log.info("Instert worker into db");
			worker = workerRepo.save(worker);
			return worker;
		}catch(Exception e) {
			log.error("Insert worker faild. ", e);
			return null;
		}
	}
}
