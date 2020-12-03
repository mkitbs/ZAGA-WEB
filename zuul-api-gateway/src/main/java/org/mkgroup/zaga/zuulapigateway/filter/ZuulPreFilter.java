package org.mkgroup.zaga.zuulapigateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class ZuulPreFilter extends ZuulFilter{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		// TODO Auto-generated method stub
		
		RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();
	    RestTemplate restTemplate = new RestTemplate();
	    String token = (request.getHeader("Authorization")).substring(7, request.getHeader("Authorization").length());
	    System.out.println(token);
	    String permisije = restTemplate.getForObject("http://localhost:8091/auth/check/{token}", String.class, token);
		String sapUserId = restTemplate.getForObject("http://localhost:8091/auth/check/{token}/sapUserId", String.class, token);
	    //String permisije = restTemplate.getForObject("http://192.168.10.206:8081/api/auth/check/{token}", String.class, token);
	    //String username = restTemplate.getForObject("http://192.168.10.206:8081/api/auth/check/{token}/username", String.class, token);
	    System.out.println(permisije);
	    System.out.println(sapUserId);
	    ctx.addZuulRequestHeader("Permissions", permisije);
	    ctx.addZuulRequestHeader("SapUserId", sapUserId);
	    ctx.addZuulRequestHeader("Token", token);
	    System.out.println(ctx);
	    
	    return null;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}