package org.mkgroup.zaga.workorderservice.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
	
	private List<LocationDTO> coordinates = new ArrayList<LocationDTO>();
	
	public FieldDTO(Field field) {
		dbId = field.getId();
		name = field.getName();
		year = field.getYear();
		area = field.getArea();
		fieldGroup = field.getFieldGroup().getId();
		erpId = field.getErpId();
		Iterator it = field.getCoordinates().entrySet().iterator();
		while (it.hasNext()) {
			   Map.Entry pair = (Map.Entry)it.next();
			   LocationDTO location = new LocationDTO();
			   String[] latLng = pair.getValue().toString().split("-");
			   System.out.println(latLng);
			   location.setLat(Double.parseDouble(latLng[0]));
			   location.setLng(Double.parseDouble(latLng[1]));
			   coordinates.add(location);
			}
	}

}
