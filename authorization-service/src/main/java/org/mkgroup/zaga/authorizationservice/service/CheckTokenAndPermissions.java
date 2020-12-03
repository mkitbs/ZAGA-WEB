package org.mkgroup.zaga.authorizationservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mkgroup.zaga.authorizationservice.jwt.JwtTokenProvider;
import org.mkgroup.zaga.authorizationservice.jwt.UserPrincipal;
import org.mkgroup.zaga.authorizationservice.model.RoleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class CheckTokenAndPermissions {

	@Value("${auth.app.jwtSecret}")
	private String jwtSecret;
	
	private static final Logger logger = LoggerFactory.getLogger(CheckTokenAndPermissions.class);
	
	@Autowired
	private JwtTokenProvider jwtProvider;
	
	public List<String> getPermissions(String authToken) throws Exception  {
		if(validateJwtToken(authToken)) {
			UserPrincipal up = jwtProvider.getUserPrincipal(authToken);
			System.out.println(up.getUsername() + " username");

			List<String> permissions = new ArrayList<String>();
			
			up.getAuthorities().forEach(role -> {
				System.out.println(role.getAuthority() + " authority");
				permissions.add(role.getAuthority().toString());
//				Role r = roleRepo.findByName(getRoleName(role.getAuthority()));
//				System.out.println("USAO U for " + r.getPermissions().size());
//				for(Permission p : r.getPermissions()) {				
//					if(!permissions.contains(p.getName())) {
//						System.out.println("USAO U IF");
//						permissions.add(p.getName());
//					}
//				}
			});
			
			return permissions;
		}
		
		return null;
	}
	
	public RoleName getRoleName(String role) {
		if(role.equals(RoleName.ROLE_SUPER_ADMIN.toString())) {
			return RoleName.ROLE_SUPER_ADMIN;
		}
		else {
			return RoleName.ROLE_MANAGER;
		}
	}
	
	public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            System.out.println(new Date() +" VREME");
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        } catch (SignatureException e) {
        	logger.error("JWT signature doews not match locally computed signature -> Message: {}", e);
		}
        
        return false;
    }
}
