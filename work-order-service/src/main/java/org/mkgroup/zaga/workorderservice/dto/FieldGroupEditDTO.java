package org.mkgroup.zaga.workorderservice.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldGroupEditDTO {

	private UUID dbid;
	private String name;
	private String companycode;
	private String organisationunit;
}
