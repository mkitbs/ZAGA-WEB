package org.mkgroup.zaga.workorderservice.dto;

import java.io.Serializable;
import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.CultureGroup;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CultureGroupDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UUID dbId;
	
	@JsonProperty("Id")
	Long id;
	
	@JsonProperty("Name")
	String name;
	
	public CultureGroupDTO(CultureGroup cultureGroup) {
		dbId = cultureGroup.getId();
		name = cultureGroup.getName();
		id = cultureGroup.getErpId();
	}

}
