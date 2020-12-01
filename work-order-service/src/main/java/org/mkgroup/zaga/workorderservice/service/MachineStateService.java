package org.mkgroup.zaga.workorderservice.service;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.workorderservice.model.MachineState;
import org.mkgroup.zaga.workorderservice.repository.MachineStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachineStateService {
	
	private static final Logger log = Logger.getLogger(MachineState.class);

	@Autowired
	MachineStateRepository machineStateRepo;
	
	public MachineState addMachineState(MachineState ms) {
		try {
			log.info("Instert machine state into db");
			ms = machineStateRepo.save(ms);
			return ms;
		} catch(Exception e) {
			log.error("Insert machine state faild. ", e);
			return null;
		}
	}
	
	
}
