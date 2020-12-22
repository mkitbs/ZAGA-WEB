package org.mkgroup.zaga.authorizationservice.dto;

import org.mkgroup.zaga.authorizationservice.model.Setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingDTO {

	private Long id;
	private boolean useSap;
	private String masterDataFormat;
	private Long tenantId;
	
	public SettingDTO(Setting setting) {
		this.id = setting.getId();
		this.useSap = setting.getUseSap();
		this.masterDataFormat = setting.getMasterDataFormat().toString();
	}
}
