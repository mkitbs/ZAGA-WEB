package org.mkgroup.zaga.authorizationservice.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenProvider tokenProvider;

	private static final Logger logger = Logger.getLogger(JwtTokenProvider.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = getJwtFromRequest(request);
		
		if (StringUtils.hasText(jwt)) {
			try {
				UserDetails details = tokenProvider.getUserPrincipal(jwt);
				
				SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities())					
				);
				
				details.getAuthorities().forEach(a -> {
					System.out.println(a);
				});
			} catch (Exception e) {
				logger.error("Exception thrown " + e.getMessage());
			}
		}

		filterChain.doFilter(request, response);
		
	}
	
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
