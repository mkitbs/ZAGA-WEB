package org.mkgroup.zaga.workorderservice.feign;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignRequestInterceptor implements RequestInterceptor  {

	@Override
	public void apply(RequestTemplate template) {
		// TODO Auto-generated method stub
		System.out.println(template.toString());
		
	}

}
