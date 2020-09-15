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
	
	@GetMapping("/getOperation/{id}")
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
		return new ResponseEntity<List<Operation>>(operationRepo.findByOrderByNameAsc(),HttpStatus.OK);
	}

}
