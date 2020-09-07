package org.mkgroup.zaga.zuulapigateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class ZuulErrorFilter extends ZuulFilter {
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		// TODO Auto-generated method stub
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		logger.info("Error Filter: " + String.format("%s request to %s from %s", request.getMethod(), request.getRequestURL().toString(), request.getLocalAddr()));
		
		return null;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "error";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}