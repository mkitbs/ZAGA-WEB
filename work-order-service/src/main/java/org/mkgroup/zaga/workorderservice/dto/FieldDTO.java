package org.mkgroup.zaga.workorderservice.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

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
	
	private UUID dbId;
	
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
	private Long fieldGroupId;
	
	private UUID fieldGroup;
	
	private Map<Double, Double> coordinates = new HashMap<Double, Double>();
	
	public FieldDTO(Field field) {
		dbId = field.getId();
		name = field.getName();
		year = field.getYear();
		area = field.getArea();
		fieldGroup = field.getFieldGroup().getId();
		erpId = field.getErpId();
		coordinates = field.getCoordinates();
	}

}
