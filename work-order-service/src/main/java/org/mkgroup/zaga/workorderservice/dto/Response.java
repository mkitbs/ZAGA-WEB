package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response {
	private Long erpId;
	private List<String> errors = new ArrayList<String>();
	private boolean success;
}
