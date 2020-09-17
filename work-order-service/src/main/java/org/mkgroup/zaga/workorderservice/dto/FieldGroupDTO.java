package org.mkgroup.zaga.workorderservice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldGroupDTO implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("Id")
	private Long id;
	
	@JsonProperty("CompanyCode")
	private String companyCode;
	
	@JsonProperty("OrganisationUnit")
	private String orgUnit;
	
	@JsonProperty("Name")
	private String name;
	
}
