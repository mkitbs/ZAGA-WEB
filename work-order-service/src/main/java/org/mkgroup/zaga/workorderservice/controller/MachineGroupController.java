package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.MachineGroupDTO;
import org.mkgroup.zaga.workorderservice.service.MachineGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/machine/group/")
public class MachineGroupController {
	
	@Autowired
	MachineGroupService machineGroupService;
	
	@GetMapping
	public ResponseEntity<?> callSAPMachineGroupSet() throws JSONException{
		return new ResponseEntity<List<MachineGroupDTO>>(
						machineGroupService.getMachineGroupsFromSAP(),
						HttpStatus.OK);
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		List<MachineGroupDTO> machineGroups = machineGroupService.getAll();
		if(machineGroups != null) {
			return new ResponseEntity<List<MachineGroupDTO>>(machineGroups, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
