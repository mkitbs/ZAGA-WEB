package org.mkgroup.zaga.workorderservice.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping("getCulture/{id}")
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
		List<Culture> cultures = cultureRepo.findByOrderByNameAsc();
		List<CultureDTO> retValues = new ArrayList<CultureDTO>();
		for(Culture culture : cultures) {
			CultureDTO c = new CultureDTO(culture);
			retValues.add(c);
		}
		return new ResponseEntity<List<CultureDTO>>(retValues, HttpStatus.OK);
	}
	
	@PostMapping("editCulture")
	public ResponseEntity<?> editCulture(@RequestBody CultureDTO cultureDTO){
		cultureService.editCulture(cultureDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("getAllByOrgCon/{orgCon}")
	public ResponseEntity<?> getAllByOrgCon(@PathVariable String orgCon){
		List<CultureDTO> cultures = cultureService.getAllByOrgCon(orgCon);
		return new ResponseEntity<List<CultureDTO>>(cultures, HttpStatus.OK);
	}
	
	@GetMapping("getAllByCultureType/{type}")
	public ResponseEntity<?> getAllByCultureType(@PathVariable String type){
		List<CultureDTO> cultures = cultureService.getAllByCultureType(type);
		return new ResponseEntity<List<CultureDTO>>(cultures, HttpStatus.OK);
	}
	
	@GetMapping("getAllByCultureGroup/{id}")
	public ResponseEntity<?> getAllByCultureGroup(@PathVariable UUID id){
		List<CultureDTO> cultures = cultureService.getAllByCultureGroup(id);
		return new ResponseEntity<List<CultureDTO>>(cultures, HttpStatus.OK);
	}
	
	@GetMapping("getAllByOrgConCultureTypeCultureGroup/{orgCon}/{type}/{id}")
	public ResponseEntity<?> getAllByOrgConCultureTypeCultureGroup(
			@PathVariable String orgCon, @PathVariable String type, @PathVariable UUID id
	){
		List<CultureDTO> cultures = cultureService.getAllByOrgConCultureTypeCultureGroup(orgCon, type, id);
		return new ResponseEntity<List<CultureDTO>>(cultures, HttpStatus.OK);
	}
	
	@GetMapping("getAllByOrgConAndCultureType/{orgCon}/{type}")
	public ResponseEntity<?> getAllByOrgConAndCultureType(@PathVariable String orgCon, @PathVariable String type){
		List<CultureDTO> cultures = cultureService.getAllByOrgConAndCultureType(orgCon, type);
		return new ResponseEntity<List<CultureDTO>>(cultures, HttpStatus.OK);
	}
	
	@GetMapping("getAllByOrgConAndCultureGroup/{orgCon}/{id}")
	public ResponseEntity<?> getAllByOrgConAndCultureGroup(@PathVariable String orgCon, @PathVariable UUID id){
		List<CultureDTO> cultures = cultureService.getAllByOrgConAndCultureGroup(orgCon, id);
		return new ResponseEntity<List<CultureDTO>>(cultures, HttpStatus.OK);
	}
	
	@GetMapping("getAllByCultureTypeAndCultureGroup/{type}/{id}")
	public ResponseEntity<?> getAllByCultureTypeAndCultureGroup(@PathVariable String type, @PathVariable UUID id){
		List<CultureDTO> cultures = cultureService.getAllByCultureTypeAndCultureGroup(type, id);
		return new ResponseEntity<List<CultureDTO>>(cultures, HttpStatus.OK);
	}
}
