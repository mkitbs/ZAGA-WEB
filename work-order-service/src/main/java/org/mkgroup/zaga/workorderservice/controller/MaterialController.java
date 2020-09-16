package org.mkgroup.zaga.workorderservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.MaterialDTO;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.mkgroup.zaga.workorderservice.service.MaterialService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/material/")
public class MaterialController {
	
	@Autowired
	MaterialService materialService;
	
	@Autowired
	MaterialRepository materialRepo;
	
	@GetMapping
	public ResponseEntity<?> callSAPEmployeeSet(){
		return new ResponseEntity<List<MaterialDTO>>(
				materialService.getMaterialsFromSAP(), HttpStatus.OK);
	}
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		List<Material> materials = materialRepo.findByOrderByNameAsc();
		List<MaterialDTO> retValues = new ArrayList<MaterialDTO>();
		for(Material material : materials) {
			MaterialDTO mat = new MaterialDTO(material);
			retValues.add(mat);
		}
		return new ResponseEntity<List<MaterialDTO>>(retValues, HttpStatus.OK);
	}
	
	@GetMapping("getMaterial/{id}")
	public ResponseEntity<?> getMaterial(@PathVariable UUID id){
		Material material = materialRepo.getOne(id);
		ModelMapper modelMapper = new ModelMapper();
		if(material != null) {
			MaterialDTO materialDTO = new MaterialDTO();
			modelMapper.map(material, materialDTO);
			return new ResponseEntity<MaterialDTO>(materialDTO, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
