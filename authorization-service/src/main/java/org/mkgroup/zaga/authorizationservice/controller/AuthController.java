package org.mkgroup.zaga.authorizationservice.controller;

import java.util.Date;

import javax.validation.Valid;

import org.mkgroup.zaga.authorizationservice.dto.ExceptionResponseDTO;
import org.mkgroup.zaga.authorizationservice.dto.LoginRequestDTO;
import org.mkgroup.zaga.authorizationservice.dto.LoginResponseDTO;
import org.mkgroup.zaga.authorizationservice.jwt.JwtTokenProvider;
import org.mkgroup.zaga.authorizationservice.repository.RoleRepository;
import org.mkgroup.zaga.authorizationservice.repository.UserRepository;
import org.mkgroup.zaga.authorizationservice.service.CheckTokenAndPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtTokenProvider jwtProvider;
    
    @Autowired
    CheckTokenAndPermissions permissions;
	
	@PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
    	
    	try {
    		
    		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
	                loginRequest.getEmail(),
	                loginRequest.getPassword()
	         );
    		
	         
    		Authentication authentication = authenticationManager.authenticate(
	            token
	        );
    		
    		/*
    		String email = authentication.getName();
    		List<String> authorities = authentication.getAuthorities().stream()
    				.map(GrantedAuthority::getAuthority)
    				.collect(Collectors.toList());
    		ProfileDTO profile = new ProfileDTO(email, authorities, true);
    		*/
    		String jwt = jwtProvider.generateToken(authentication);
    		
    		
	        return new ResponseEntity<LoginResponseDTO>(new LoginResponseDTO(jwt), HttpStatus.OK);
		} catch (AuthenticationException e) {

			return new ResponseEntity<ExceptionResponseDTO>(
					new ExceptionResponseDTO(new Date(), 
							e.getMessage(), 
							"Nevalidni kredencijali."), 
					HttpStatus.UNAUTHORIZED);
		}
    }
}
