package org.mkgroup.zaga.workorderservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.CropDTO;
import org.mkgroup.zaga.workorderservice.model.Crop;
import org.mkgroup.zaga.workorderservice.repository.CropRepository;
import org.mkgroup.zaga.workorderservice.service.CropService;
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
@RequestMapping("/api/crop/")
public class CropController {

	@Autowired
	CropService cropService;
	
	@Autowired
	CropRepository cropRepo;
	
	@GetMapping
	public ResponseEntity<?> callSAPEmployeeSet(){
		return new ResponseEntity<List<CropDTO>>(
				cropService.getCropsFromSAP(), HttpStatus.OK);
	}
	
	@GetMapping("getCrop/{id}")
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
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		List<Crop> crops = cropRepo.findByOrderByNameAsc();
		List<CropDTO> retValues = new ArrayList<CropDTO>();
		for(Crop crop : crops) {
			CropDTO c = new CropDTO(crop);
			retValues.add(c);
		}
		return new ResponseEntity<List<CropDTO>>(retValues, HttpStatus.OK);
	}
	
	@GetMapping("getAllByFieldAndYear/{fieldId}/{year}")
	public ResponseEntity<?> getAllByFieldAndYear(@PathVariable UUID fieldId, @PathVariable int year){
		List<CropDTO> crops = cropService.getAllByFieldAndYear(fieldId, year);
		if(crops != null) {
			return new ResponseEntity<List<CropDTO>>(crops, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("getAllByFieldAndCulture/{fieldId}/{cultureId}")
	public ResponseEntity<?> getAllByFieldAndCulture(@PathVariable UUID fieldId, @PathVariable UUID cultureId){
		List<CropDTO> crops = cropService.getAllByFieldAndCulture(fieldId, cultureId);
		if(crops != null) {
			return new ResponseEntity<List<CropDTO>>(crops, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("getAllByField/{fieldId}")
	public ResponseEntity<?> getAllByField(@PathVariable UUID fieldId){
		List<CropDTO> crops = cropService.getAllByField(fieldId);
		if(crops != null) {
			return new ResponseEntity<List<CropDTO>>(crops, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("getAllByCulture/{cultureId}")
	public ResponseEntity<?> getAllByCulture(@PathVariable UUID cultureId){
		List<CropDTO> crops = cropService.getAllByCulture(cultureId);
		if(crops != null) {
			return new ResponseEntity<List<CropDTO>>(crops, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("updateCrop")
	public ResponseEntity<?> updateCrop(@RequestBody CropDTO cropDTO){
		cropService.editCrop(cropDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
