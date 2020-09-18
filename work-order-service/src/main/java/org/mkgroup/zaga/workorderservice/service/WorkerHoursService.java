package org.mkgroup.zaga.workorderservice.service;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.model.WorkerHours;
import org.mkgroup.zaga.workorderservice.repository.WorkerHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerHoursService {
	
	private static final Logger log = Logger.getLogger(WorkerHours.class);

	@Autowired
	WorkerHoursRepository workerHoursRepo;
	
	public WorkerHours addWorkerHours(WorkerHours wh) {
		try {
			log.info("Instert worker hours into db");
			wh = workerHoursRepo.save(wh);
			return wh;
		} catch(Exception e) {
			log.error("Insert worker hours faild. ", e);
			return null;
		}
	}
}
