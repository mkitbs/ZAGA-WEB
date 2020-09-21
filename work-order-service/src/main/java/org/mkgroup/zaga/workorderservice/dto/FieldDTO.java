package org.mkgroup.zaga.workorderservice.dto;

import java.io.Serializable;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDTO implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UUID id;
	
	@JsonProperty("Id")
	private Long erpId;
	
	@JsonProperty("CompanyCode")
	private String companyCode;
	
	@JsonProperty("OrganisationUnit")
	private String orgUnit;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Year")
	private int year;
	
	@JsonProperty("Area")
	private double area;
	
	@JsonProperty("FieldGroupId")
	private Long fieldGroup;
	
	public FieldDTO(Field field) {
		id = field.getId();
		name = field.getName();
	}

}