package org.mkgroup.zaga.workorderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropVarietyDTO {

	@JsonProperty("Id")
	private Long id;
	
	@JsonProperty("CompanyCode")
	private String companyCode;
    	
	@JsonProperty("OrganisationUnit")
	private String organisationUnit;
	
	@JsonProperty("CropId")
	private Long cropId;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Area")
	private double area;
    
}
