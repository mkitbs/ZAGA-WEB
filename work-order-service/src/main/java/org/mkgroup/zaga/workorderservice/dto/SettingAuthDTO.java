package org.mkgroup.zaga.workorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingAuthDTO {

	private Long id;
	private boolean useSap;
	private String masterDataFormat;
	private Long tenantId;
}
