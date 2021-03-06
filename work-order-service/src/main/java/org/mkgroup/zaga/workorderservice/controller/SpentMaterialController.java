package org.mkgroup.zaga.workorderservice.controller;

import java.util.List;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.dto.MaterialReportDTO;
import org.mkgroup.zaga.workorderservice.dto.MaterialReportHelperDTO;
import org.mkgroup.zaga.workorderservice.dto.Response;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialPerCultureDTO;
import org.mkgroup.zaga.workorderservice.dto.SpentMaterialPerCultureReportDTO;
import org.mkgroup.zaga.workorderservice.dtoSAP.SAPResponse;
import org.mkgroup.zaga.workorderservice.model.Material;
import org.mkgroup.zaga.workorderservice.model.SpentMaterial;
import org.mkgroup.zaga.workorderservice.model.WorkOrder;
import org.mkgroup.zaga.workorderservice.repository.MachineRepository;
import org.mkgroup.zaga.workorderservice.repository.MaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.SpentMaterialRepository;
import org.mkgroup.zaga.workorderservice.repository.WorkOrderRepository;
import org.mkgroup.zaga.workorderservice.service.SpentMaterialService;
import org.mkgroup.zaga.workorderservice.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spentMaterial/")
public class SpentMaterialController {

	@Autowired
	SpentMaterialRepository spentMaterialRepo;
	
	@Autowired
	WorkOrderRepository workOrderRepo;
	
	@Autowired
	MaterialRepository materialRepo;
	
	@Autowired
	WorkOrderService workOrderService;
	
	@Autowired
	SpentMaterialService spentMaterialService;
	
	@PostMapping("{id}")
	public ResponseEntity<?> addSpentMaterial(@PathVariable UUID id, @RequestBody SpentMaterialDTO spentMaterialDTO){
		try {
			spentMaterialService.addSpentMaterial(id, spentMaterialDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("updateSpentMaterial/{id}")
	public ResponseEntity<?> updateSpentMaterial(@PathVariable UUID id, @RequestBody SpentMaterialDTO spentMaterialDTO) throws Exception{
		
		Response resp = spentMaterialService.updateSpentMaterial(id, spentMaterialDTO);
		if(resp.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<Response>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("deleteSpentMaterial/{id}")
	public ResponseEntity<?> deleteSpentMaterial(@PathVariable UUID id){
		spentMaterialService.deleteSpentMaterial(id);
		SpentMaterial sp = spentMaterialRepo.getOne(id);
		try {
			SAPResponse response = workOrderService.updateDataWorkOrder(sp.getWorkOrder());
			return new ResponseEntity<SAPResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SAPResponse response = new SAPResponse();
			response.setSuccess(false);
			response.getMessage().add(e.getMessage());
			return new ResponseEntity<SAPResponse>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("updateSpentMaterialBasicInfo/{id}")
	public ResponseEntity<?> updateSpentMaterialBasicInfo(@PathVariable UUID id, @RequestBody SpentMaterialDTO spentMaterialDTO) throws Exception{
		
		Response resp = spentMaterialService.updateMaterialBasicInfo(id, spentMaterialDTO);
		if(resp.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<Response>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("getDataForReport")
	public ResponseEntity<?> getDataForReport(@RequestHeader("TenantId") String tenantId){
		List<MaterialReportDTO> data = spentMaterialService.getMaterialsForReport(Long.parseLong(tenantId));
		return new ResponseEntity<List<MaterialReportDTO>>(data, HttpStatus.OK);
	}
	
	@GetMapping("getSpentMaterialPerCulture")
	public ResponseEntity<?> getSpentMaterialPerCulture(@RequestHeader("TenantId") String tenantId){
		List<SpentMaterialPerCultureReportDTO> spentMaterials = spentMaterialService.getSpentMaterialPerCulture(Long.parseLong(tenantId));
		return new ResponseEntity<List<SpentMaterialPerCultureReportDTO>>(spentMaterials, HttpStatus.OK);
	}
}
