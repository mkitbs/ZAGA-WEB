package org.mkgroup.zaga.workorderservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.MachineDTO;
import org.mkgroup.zaga.workorderservice.dto.MachineReportDTO;
import org.mkgroup.zaga.workorderservice.model.Machine;
import org.mkgroup.zaga.workorderservice.model.MachineType;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.service.MachineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	
	@GetMapping("getAll/propulsion")
	public ResponseEntity<?> getPropulsion(){
		List<Machine> machines = machineRepo.getMachines(MachineType.PROPULSION.toString());
		List<MachineDTO> retVal = new ArrayList<MachineDTO>();
		for(Machine machine : machines) {
			MachineDTO macDTO = new MachineDTO(machine);
			retVal.add(macDTO);
		}
		return new ResponseEntity<List<MachineDTO>>(retVal, HttpStatus.OK);
	}
	
	@GetMapping("getAll/coupling")
	public ResponseEntity<?> getCoupling(){
		List<Machine> machines = machineRepo.getMachines(MachineType.COUPLING.toString());
		List<MachineDTO> retVal = new ArrayList<MachineDTO>();
		for(Machine machine : machines) {
			MachineDTO macDTO = new MachineDTO(machine);
			retVal.add(macDTO);
		}
		return new ResponseEntity<List<MachineDTO>>(retVal, HttpStatus.OK);
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
	
	@PostMapping("editMachine")
	public ResponseEntity<?> editMachine(@RequestBody MachineDTO machineDTO){
		machineService.editMachine(machineDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("getMachinesByMachineGroup/{id}")
	public ResponseEntity<?> getMachinesByMachineGroup(@PathVariable UUID id){
		List<MachineDTO> machines = machineService.getAllByGroup(id);
		return new ResponseEntity<List<MachineDTO>>(machines, HttpStatus.OK);
	}
	
	@GetMapping("getDataForReport")
	public ResponseEntity<?> getDataForReport(@RequestHeader("SapUserId") String sapuserid){
		List<MachineReportDTO> data = machineService.getMachinesForReport(sapuserid);
		return new ResponseEntity<List<MachineReportDTO>>(data, HttpStatus.OK);
	}
	
	@GetMapping("getAllGroupByType")
	public ResponseEntity<?> getAllGroupByType(){
		List<MachineDTO> machines = machineService.getAllGrouByMachineType();
		return new ResponseEntity<List<MachineDTO>>(machines, HttpStatus.OK);
	}
}
