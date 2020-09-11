package org.mkgroup.zaga.workorderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CropDTO {

	@JsonProperty("Id")
	private Long id;
	
	@JsonProperty("CultureId")
	private Long cultureId;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("OrganisationUnit")
	private String organisationUnit;
	
	@JsonProperty("CompanyCode")
	private String companyCode;
	
	@JsonProperty("Area")
	private double area;
	
	@JsonProperty("Year")
	private int year;
	
	@JsonProperty("FieldId")
	private Long fieldId;
	
}
