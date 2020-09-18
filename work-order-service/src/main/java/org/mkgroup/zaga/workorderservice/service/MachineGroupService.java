package org.mkgroup.zaga.workorderservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONException;
import org.mkgroup.zaga.workorderservice.configuration.SAPAuthConfiguration;
import org.mkgroup.zaga.workorderservice.dto.MachineGroupDTO;
import org.mkgroup.zaga.workorderservice.feign.SAPGatewayProxy;
import org.mkgroup.zaga.workorderservice.model.MachineGroup;
import org.mkgroup.zaga.workorderservice.odata.ODataToDTOConvertor;
import org.mkgroup.zaga.workorderservice.repository.MachineGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MachineGroupService {
	
	@Autowired
	SAPGatewayProxy gwProxy;
	
	@Autowired
	SAPAuthConfiguration authConfiguration;
	
	@Autowired
	ODataToDTOConvertor odataConvertor;
	
	@Autowired
	MachineGroupRepository machineGroupRepo;
	
	public List<MachineGroupDTO> getMachineGroupsFromSAP() throws JSONException{
		//Authorization String to Encode
		StringBuilder authEncodingString = new StringBuilder()
				.append(authConfiguration.getUsername())
				.append(":")
				.append(authConfiguration.getPassword());
		//Encoding Authorization String
		String authHeader = Base64.getEncoder().encodeToString(
				authEncodingString.toString().getBytes());
		//Call SAP and retrieve machineGroupSet
		ResponseEntity<Object> machineGroupSet = 
				gwProxy.fetchMachineGroups("json", "Basic " + authHeader);
		String oDataString = machineGroupSet.getBody().toString().replace(":", "-");
		oDataString = formatJSON(oDataString);
		//Map to specific object
		ArrayList<MachineGroupDTO> machineGroupList = 
									convertObjectToLocalList(odataConvertor
																.convertODataSetToDTO
																		(oDataString));
		
		for(MachineGroupDTO machine : machineGroupList) {
			machineGroupRepo
				.findByErpId(machine.getErpId())
				.ifPresentOrElse(foundMachineGroup -> updateMachineGroup(foundMachineGroup, machine),
								() -> createMachineGroup(machine));
		}
		
		return machineGroupList;
	}

	private ArrayList<MachineGroupDTO> convertObjectToLocalList(Object listAsObject) {
		List<?> list = (List<?>) listAsObject;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		ArrayList<MachineGroupDTO> convertedList = new ArrayList<MachineGroupDTO>();
		list.forEach(objectOfAList -> {
			MachineGroupDTO machineGroupDTO = new MachineGroupDTO();
			
			try {
				machineGroupDTO = objectMapper
						
												.readValue(objectOfAList.toString(),
														MachineGroupDTO.class);
				convertedList.add(machineGroupDTO);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		return convertedList;
	}

	public String formatJSON(String json) {
		json = json.replace("=", ":");
		json = json.replaceAll("__metadata:\\{[a-zA-Z0-9,':=\".()/_ -]*\\},", "");
		json = json.replace("/", "");
		json = json.replaceAll(":,", ":\"\",");
		json = json.replaceAll(":}", ":\"\"}");
		return json;
	}
	
	private void createMachineGroup(MachineGroupDTO machine) {
		MachineGroup m = new MachineGroup(machine);
		machineGroupRepo.save(m);
	}

	private void updateMachineGroup(MachineGroup oldMachineGroup, MachineGroupDTO updatedMachineGroup) {
		oldMachineGroup.setName(updatedMachineGroup.getName());
		machineGroupRepo.save(oldMachineGroup);
	}
}
