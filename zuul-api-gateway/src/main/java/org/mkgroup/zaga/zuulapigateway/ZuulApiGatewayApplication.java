package org.mkgroup.zaga.zuulapigateway;

import org.mkgroup.zaga.zuulapigateway.filter.ZuulErrorFilter;
import org.mkgroup.zaga.zuulapigateway.filter.ZuulPostFilter;
import org.mkgroup.zaga.zuulapigateway.filter.ZuulPreFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@EnableZuulProxy
@EnableCircuitBreaker
@EnableHystrix
@SpringBootApplication
public class ZuulApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGatewayApplication.class, args);
	}
	
	@Bean
	public ZuulPreFilter preFilter() {
	    return new ZuulPreFilter();
    }
	
	@Bean
	public ZuulPostFilter postFilter() {
	    return new ZuulPostFilter();
	}

	@Bean
	public ZuulErrorFilter errorFilter() {
	    return new ZuulErrorFilter();
	}

}
