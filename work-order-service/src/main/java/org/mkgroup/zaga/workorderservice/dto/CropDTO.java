package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CropDTO {
	
	private UUID id;

	@JsonProperty("Id")
	private Long erpId;
	
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
