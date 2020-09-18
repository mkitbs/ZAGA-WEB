package org.mkgroup.zaga.workorderservice.feign;

import java.util.List;

import org.mkgroup.zaga.workorderservice.dto.UserElasticDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SearchServiceFallback implements SearchServiceProxy{

	@Override
	public ResponseEntity<?> sendEmployees(List<UserElasticDTO> allUsers) {
		// TODO Auto-generated method stub
		return null;
	}

}
