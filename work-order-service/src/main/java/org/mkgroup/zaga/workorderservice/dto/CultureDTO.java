package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Culture;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CultureDTO {
	
	private UUID dbId;
	
	@JsonProperty("Id")
    Long erpId;
	
	@JsonProperty("CultureGroupId")
    Long cultureGroupId;
	
	@JsonProperty("Name")
    String name;
	
	@JsonProperty("OrgKon")
    String orgCon;
	
	@JsonProperty("Type")
    String type;
	
	private UUID cultureGroup;
	
	public CultureDTO(Culture culture) {
		dbId = culture.getId();
		name = culture.getName();
		cultureGroup = culture.getCultureGroup().getId();
		orgCon = culture.getOrgCon().toString();
		type = culture.getType().toString();
	}

}
