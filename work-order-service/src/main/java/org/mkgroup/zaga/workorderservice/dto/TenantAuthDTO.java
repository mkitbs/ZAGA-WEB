package org.mkgroup.zaga.workorderservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantAuthDTO {

	private Long id;
	private String name;
	private SettingAuthDTO setting;
	private List<UserAuthDTO> users;
	private String companyCode;
}
