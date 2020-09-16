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
	
	private UUID id;
	
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
	
	public CultureDTO(Culture culture) {
		id = culture.getId();
		name = culture.getName();
	}

}
