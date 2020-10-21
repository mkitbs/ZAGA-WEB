package org.mkgroup.zaga.workorderservice.feign;

import java.util.List;

import org.mkgroup.zaga.workorderservice.dto.UserElasticDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="search-service", 
			url="http://localhost:8855/", 
			fallback = SearchServiceFallback.class)
public interface SearchServiceProxy {

	@PostMapping(value = "/api/users/")
	public ResponseEntity<?> sendEmployees(List<UserElasticDTO> allUsers);
	
	@PostMapping(value = "/api/users/editUser")
	public ResponseEntity<?> editEmployee(UserElasticDTO user);
}
