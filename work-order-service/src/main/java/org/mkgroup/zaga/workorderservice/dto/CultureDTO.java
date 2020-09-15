package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CultureDTO {
	
	private UUID id;
	
	private String name;
	
	@JsonProperty("Id")
    Long erpId;
	
	@JsonProperty("CultureGroupId")
    Long cultureGroupId;
	
	@JsonProperty("OrgKon")
    String orgCon;
	
	@JsonProperty("Type")
    String type;
}
