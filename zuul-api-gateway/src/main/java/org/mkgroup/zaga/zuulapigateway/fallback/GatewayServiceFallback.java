package org.mkgroup.zaga.zuulapigateway.fallback;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netflix.hystrix.exception.HystrixTimeoutException;

@Component
public class GatewayServiceFallback implements FallbackProvider {

    @Override
    public String getRoute() {
        return "*"; // or return null;
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
    	/* logging here */

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
		        .getRequest();
		String serviceId = request.getRequestURI().split("/")[1];
		String message = serviceId + "'s information is not available.";
        if (cause instanceof HystrixTimeoutException) {
            return new GatewayClientResponse(HttpStatus.GATEWAY_TIMEOUT, message);
        } else {
            return new GatewayClientResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

}