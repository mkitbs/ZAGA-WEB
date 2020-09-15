package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.dto.CultureDTO;
import org.mkgroup.zaga.workorderservice.model.Culture;
import org.mkgroup.zaga.workorderservice.repository.CultureRepository;
import org.mkgroup.zaga.workorderservice.service.CultureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/culture/")
public class CultureController {
	
	@Autowired
	CultureService cultureService;
	
	@Autowired
	CultureRepository cultureRepo;

	@GetMapping
	public ResponseEntity<?> callSAPCultureSet() throws JSONException {
		
		return new ResponseEntity<List<CultureDTO>>(
				cultureService.getCulturesFromSAP(),
							HttpStatus.OK);
	}
	
	@GetMapping("/getCulture/{id}")
	public ResponseEntity<?> getCulture(@PathVariable UUID id){
		Culture culture = cultureService.getOne(id);
		ModelMapper modelMapper = new ModelMapper();
		if(culture != null) {
			CultureDTO cultureDTO = new CultureDTO();
			modelMapper.map(culture, cultureDTO);
			return new ResponseEntity<CultureDTO>(cultureDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		return new ResponseEntity<List<Culture>>(cultureRepo.findByOrderByNameAsc(),HttpStatus.OK);
	}
}
