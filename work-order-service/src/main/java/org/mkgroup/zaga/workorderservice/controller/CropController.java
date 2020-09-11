package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.CropDTO;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.service.CropService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crop/")
public class CropController {

	@Autowired
	CropService cropService;
	
	@GetMapping
	public ResponseEntity<?> callSAPEmployeeSet(){
		return new ResponseEntity<List<CropDTO>>(
				cropService.getCropsFromSAP(), HttpStatus.OK);
	}
	
	@GetMapping("/getCrop/{id}")
	public ResponseEntity<?> getCrop(@PathVariable UUID id){
		Crop crop = cropService.getOne(id);
		ModelMapper modelMapper = new ModelMapper();
		if(crop != null) {
			CropDTO cropDTO = new CropDTO();
			modelMapper.map(crop, cropDTO);
			return new ResponseEntity<CropDTO>(cropDTO, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
