package org.mkgroup.zaga.workorderservice.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldPolygonDTO {

	private UUID id;
	private List<LocationDTO> values = new ArrayList<LocationDTO>();
}
