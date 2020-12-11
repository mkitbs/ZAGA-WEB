package org.mkgroup.zaga.workorderservice.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldPolygonDTO {

	private UUID id;
	private Map<Double, Double> values = new HashMap<Double, Double>();
}
