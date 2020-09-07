package org.mkgroup.zaga.zuulapigateway.filter;

import org.jboss.logging.Logger;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class ZuulPostFilter extends ZuulFilter{

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		
		logger.info("Post Filter: " + String.format("Response Status Code: %s", context.getResponse().getStatus()));
		
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		return "post";
	}

}
