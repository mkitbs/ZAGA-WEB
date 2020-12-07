package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.Variety;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VarietyDTO {
	
	private UUID dbId;
	
	private UUID culture;

	@JsonProperty("Id")
	private Long id;
	
	@JsonProperty("CultureId")
	private Long cultureId;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Manufacturer")
	private String manufacturer;
	
	@JsonProperty("Finishing")
	private String finishing;
	
	@JsonProperty("Protection")
	private String protection;
	
	private String cultureName;
	

	public VarietyDTO(Variety variety) {
		dbId = variety.getId();
		name = variety.getName();
		manufacturer = variety.getManufacturer();
		finishing = variety.getFinishing();
		protection = variety.getProtection();
		culture = variety.getCulture().getId();
		id = variety.getErpId();
		cultureId = variety.getCulture().getErpId();
		cultureName = variety.getCulture().getName();
		
	}

}

