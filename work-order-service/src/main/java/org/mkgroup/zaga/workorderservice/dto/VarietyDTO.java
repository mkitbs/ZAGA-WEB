package org.mkgroup.zaga.workorderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VarietyDTO {

	@JsonProperty("Id")
	private Long id;
	
	@JsonProperty("CultureId")
	private Long cultureId;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Manufacturer")
	private String manufacturer;
	
	@JsonProperty("Finishing")
	private String finishing;
	
	@JsonProperty("Protection")
	private String protection;
}
