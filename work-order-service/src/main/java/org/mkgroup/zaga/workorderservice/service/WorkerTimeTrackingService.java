package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.TimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkOrderTractorDriverDTO;
import org.mkgroup.zaga.workorderservice.dto.WorkerTimeTrackingDTO;
import org.mkgroup.zaga.workorderservice.model.TimeTrackingType;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorker;
import org.mkgroup.zaga.workorderservice.model.WorkOrderWorkerStatus;
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
	
	public UUID setTracking(TimeTrackingDTO timeTracking) {
		if(timeTracking.getId() == null || timeTracking.getId().equals("")) {
			WorkerTimeTracking wtt = new WorkerTimeTracking();
			WorkOrderWorker wo = wowRepo.getOne(timeTracking.getWowId());
			wtt.setWorkOrderWorker(wo);
			wtt.setStartTime(timeTracking.getStartTime());
			wtt.setEndTime(timeTracking.getEndTime());
			if(timeTracking.getType().equals("RN")) {
				wtt.setType(TimeTrackingType.RN);
				wo.setStatus(WorkOrderWorkerStatus.STARTED);
			}else if(timeTracking.getType().equals("PAUSE_WORK")) {
				wtt.setType(TimeTrackingType.PAUSE_WORK);
				wo.setStatus(WorkOrderWorkerStatus.PAUSED);
			}else if(timeTracking.getType().equals("PAUSE_SERVICE")) {
				wtt.setType(TimeTrackingType.PAUSE_SERVICE);
				wo.setStatus(WorkOrderWorkerStatus.PAUSED);
			}else if(timeTracking.getType().equals("PAUSE_FUEL")) {
				wtt.setType(TimeTrackingType.PAUSE_FUEL);
				wo.setStatus(WorkOrderWorkerStatus.PAUSED);
			}
			wtt = timeTrackingRepo.save(wtt);
			wowRepo.save(wo);
			return wtt.getId();
		}else {
			WorkerTimeTracking wtt = timeTrackingRepo.getOne(timeTracking.getId());
			WorkOrderWorker wo = wowRepo.getOne(timeTracking.getWowId());
			wtt.setEndTime(timeTracking.getEndTime());
			if(timeTracking.getType().startsWith("PAUSE")) {
				wo.setStatus(WorkOrderWorkerStatus.STARTED);
			} else {
				wtt.setType(TimeTrackingType.FINISHED);
				wo.setStatus(WorkOrderWorkerStatus.FINISHED);
			}
			wtt = timeTrackingRepo.save(wtt);
			wowRepo.save(wo);
			return wtt.getId();
		}
		
	}
}
