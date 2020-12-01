package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Crop;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CropDTO {
	
	private UUID id;
	
	private UUID cultureId;
	
	private UUID fieldId;
	
	private String fieldName;
	
	private String cultureName;

	@JsonProperty("Id")
	private Long erpId;
	
	@JsonProperty("CultureId")
	private Long erpCultureId;
	
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
	private Long erpFieldId;
	
	public CropDTO(Crop crop) {

		id = crop.getId();
		name = crop.getName();
		area = crop.getArea();
		year = crop.getYear();
		fieldId = crop.getField().getId();
		cultureId = crop.getCulture().getId();
		erpId = crop.getErpId();
		fieldName = crop.getField().getName();
		cultureName = crop.getCulture().getName();
		erpFieldId = crop.getField().getErpId();
		erpCultureId = crop.getCulture().getErpId();
	}

}
