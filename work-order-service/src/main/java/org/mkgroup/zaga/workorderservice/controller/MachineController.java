package org.mkgroup.zaga.workorderservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.MachineDTO;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.service.MachineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/machine/")
public class MachineController {

	@Autowired
	MachineService machineService;
	
	@Autowired
	MachineRepository machineRepo;
	
	@GetMapping
	public ResponseEntity<?> callSAPEmployeeSet(){
		return new ResponseEntity<List<MachineDTO>>(
				machineService.getMachinesFromSAP(), HttpStatus.OK);
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		List<Machine> machines = machineRepo.findByOrderByNameAsc();
		List<MachineDTO> retValues = new ArrayList<MachineDTO>();
		for(Machine machine : machines) {
			MachineDTO m = new MachineDTO(machine);
			retValues.add(m);
		}
		return new ResponseEntity<List<MachineDTO>>(retValues, HttpStatus.OK);
	}
	
	@GetMapping("getMachine/{id}")
	public ResponseEntity<?> getMachine(@PathVariable UUID id){
		Machine machine = machineService.getOne(id);
		ModelMapper modelMapper = new ModelMapper();
		if(machine != null) {
			MachineDTO machineDTO = new MachineDTO();
			modelMapper.map(machine, machineDTO);
			return new ResponseEntity<MachineDTO>(machineDTO, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}