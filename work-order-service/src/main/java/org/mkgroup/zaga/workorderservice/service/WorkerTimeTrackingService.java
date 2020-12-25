package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.TimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderTractorDriverDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerTimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.model.WorkerTimeTracking;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderWorkerRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkerTimeTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerTimeTrackingService {

	@Autowired
	WorkerTimeTrackingRepository timeTrackingRepo;
	
	@Autowired
	WorkOrderWorkerRepository wowRepo;
	
	public WorkerTimeTrackingDTO getOne(UUID wowId) {
		WorkOrderWorker wow = wowRepo.getOne(wowId);
		List<WorkerTimeTracking> times = timeTrackingRepo.findByWorkOrderWorkerId(wowId);
		WorkerTimeTrackingDTO retValue = new WorkerTimeTrackingDTO();
		retValue.setHeaderInfo(new WorkOrderTractorDriverDTO(wow));
		List<TimeTrackingDTO> timesDTO = new ArrayList<TimeTrackingDTO>();
		for(WorkerTimeTracking time : times) {
			TimeTrackingDTO timeDTO = new TimeTrackingDTO(time);
			timesDTO.add(timeDTO);
		}
		retValue.setTimes(timesDTO);
		return retValue;
		
		
	}
}
