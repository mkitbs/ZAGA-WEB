package org.mkgroup.zaga.workorderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CultureDTO {
	
	
	@JsonProperty("Id")
    Long id;
	
	@JsonProperty("CultureGroupId")
    Long cultureGroupId;
	
	@JsonProperty("Name")
    String name;
	
	@JsonProperty("OrgKon")
    String orgKon;
	
	@JsonProperty("Type")
    String type;
}
