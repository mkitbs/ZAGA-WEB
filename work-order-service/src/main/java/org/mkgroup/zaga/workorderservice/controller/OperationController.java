package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.OperationDTO;
import org.mkgroup.zaga.workorderservice.model.Operation;
import org.mkgroup.zaga.workorderservice.repository.OperationRepository;
import org.mkgroup.zaga.workorderservice.service.OperationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operation/")
public class OperationController {
	
	@Autowired
	OperationService operationService;
	
	@Autowired
	OperationRepository operationRepo;
	
	@GetMapping
	public ResponseEntity<?> callSAPOperationSet() throws JSONException {
		return new ResponseEntity<List<OperationDTO>>(
							operationService.getOperationsFromSAP(),
							HttpStatus.OK);
	}
	
	@GetMapping("getOperation/{id}")
	public ResponseEntity<?> getOperation(@PathVariable UUID id){
		Operation operation = operationService.getOne(id);
		ModelMapper modelMapper = new ModelMapper();
		if(operation != null) {
			OperationDTO operationDTO = new OperationDTO();
			modelMapper.map(operation, operationDTO);
			return new ResponseEntity<OperationDTO>(operationDTO, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		return new ResponseEntity<List<OperationDTO>>(operationService.getAll(), HttpStatus.OK);
	}
	
	@PostMapping("editOperation")
	public ResponseEntity<?> editOperation(@RequestBody OperationDTO operationDTO){
		operationService.editOperation(operationDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("getAllByTypeAndGroup/{type}/{groupId}")
	public ResponseEntity<?> getAllByTypeAndGroup(@PathVariable String type, @PathVariable UUID groupId){
		List<OperationDTO> operations = operationService.getAllByTypeAndGroup(type, groupId);
		return new ResponseEntity<List<OperationDTO>>(operations, HttpStatus.OK);
	}
	
	@GetMapping("getAllByType/{type}")
	public ResponseEntity<?> getAllByType(@PathVariable String type){
		List<OperationDTO> operations = operationService.getAllByType(type);
		return new ResponseEntity<List<OperationDTO>>(operations, HttpStatus.OK);
	}
	
	@GetMapping("getAllByGroup/{groupId}")
	public ResponseEntity<?> getAllByGroup(@PathVariable UUID groupId){
		List<OperationDTO> operations = operationService.getAllByGroup(groupId);
		return new ResponseEntity<List<OperationDTO>>(operations, HttpStatus.OK);
	}
	
	@GetMapping("getAllByErpGroup/{groupId}")
	public ResponseEntity<?> getAllByGroup(@PathVariable Long groupId){
		List<OperationDTO> operations = operationService.getAllByErpGroup(groupId);
		return new ResponseEntity<List<OperationDTO>>(operations, HttpStatus.OK);
	}
	
	@GetMapping("getAllGroupByType")
	public ResponseEntity<?> getAllGroupByType(){
		List<OperationDTO> operations = operationService.getAllGroupByType();
		return new ResponseEntity<List<OperationDTO>>(operations, HttpStatus.OK);
	}

}
