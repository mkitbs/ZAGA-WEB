package org.mkgroup.zaga.workorderservice.dto;

import java.io.Serializable;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.FieldGroup;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldGroupDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UUID dbId;
	
	@JsonProperty("Id")
	private Long id;
	
	@JsonProperty("CompanyCode")
	private String companyCode;
	
	@JsonProperty("OrganisationUnit")
	private String orgUnit;
	
	@JsonProperty("Name")
	private String name;
	
	public FieldGroupDTO(FieldGroup field) {
		dbId = field.getId();
		name = field.getName();
		companyCode = field.getCompanyCode();
		orgUnit = field.getOrgUnit();
	}
}
