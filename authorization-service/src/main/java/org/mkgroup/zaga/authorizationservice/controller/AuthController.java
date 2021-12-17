package org.mkgroup.zaga.authorizationservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.mkgroup.zaga.authorizationservice.dto.ExceptionResponseDTO;
import org.mkgroup.zaga.authorizationservice.dto.LoginRequestDTO;
import org.mkgroup.zaga.authorizationservice.dto.LoginResponseDTO;
import org.mkgroup.zaga.authorizationservice.dto.ResetPasswordDTO;
import org.mkgroup.zaga.authorizationservice.dto.RoleDTO;
import org.mkgroup.zaga.authorizationservice.dto.SettingDTO;
import org.mkgroup.zaga.authorizationservice.dto.SignupRequestDTO;
import org.mkgroup.zaga.authorizationservice.dto.UserDTO;
import org.mkgroup.zaga.authorizationservice.exception.InvalidJTWTokenException;
import org.mkgroup.zaga.authorizationservice.jwt.JwtTokenProvider;
import org.mkgroup.zaga.authorizationservice.model.PasswordResetToken;
import org.mkgroup.zaga.authorizationservice.model.Role;
import org.mkgroup.zaga.authorizationservice.model.RoleName;
import org.mkgroup.zaga.authorizationservice.model.Setting;
import org.mkgroup.zaga.authorizationservice.model.Tenant;
import org.mkgroup.zaga.authorizationservice.model.User;
import org.mkgroup.zaga.authorizationservice.repository.PasswordResetTokenRepository;
import org.mkgroup.zaga.authorizationservice.repository.RoleRepository;
import org.mkgroup.zaga.authorizationservice.repository.SettingRepository;
import org.mkgroup.zaga.authorizationservice.repository.TenantRepository;
import org.mkgroup.zaga.authorizationservice.repository.UserRepository;
import org.mkgroup.zaga.authorizationservice.service.CheckTokenAndPermissions;
import org.mkgroup.zaga.authorizationservice.service.MailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@Autowired
	MailNotification mailNotification;

	@Autowired
	TenantRepository tenantRepository;

	@Autowired
	SettingRepository settingRepository;

	@Autowired
	PasswordResetTokenRepository passwordResetRepo;

	private static final Logger logger = Logger.getLogger(AuthController.class);

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {

		try {

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
					loginRequest.getPassword());

			Authentication authentication = authenticationManager.authenticate(token);

			User user = userRepository.findByEmail(loginRequest.getEmail()).get();
			List<RoleDTO> roles = new ArrayList<RoleDTO>();
			for (Role role : user.getRoles()) {
				RoleDTO r = new RoleDTO();
				r.setName(role.getName());
				roles.add(r);
			}

			/*
			 * String email = authentication.getName(); List<String> authorities =
			 * authentication.getAuthorities().stream() .map(GrantedAuthority::getAuthority)
			 * .collect(Collectors.toList()); ProfileDTO profile = new ProfileDTO(email,
			 * authorities, true);
			 */
			String jwt = jwtProvider.generateToken(authentication);

			return new ResponseEntity<LoginResponseDTO>(new LoginResponseDTO(jwt, roles), HttpStatus.OK);
		} catch (AuthenticationException e) {

			return new ResponseEntity<ExceptionResponseDTO>(
					new ExceptionResponseDTO(new Date(), e.getMessage(), "Nevalidni kredencijali."),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/check/{token}")
	public ResponseEntity<?> checkToken(@PathVariable String token) throws InvalidJTWTokenException {
		if (permissions.validateJwtToken(token)) {
			String s = "";
			try {
				for (GrantedAuthority str : jwtProvider.getUserPrincipal(token).getAuthorities()) {
					s += str.getAuthority() + '|';
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<String>(s, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/check/{token}/sapUserId")
	public ResponseEntity<?> getUsername(@PathVariable String token) throws InvalidJTWTokenException {
		if (permissions.validateJwtToken(token)) {
			try {
				return new ResponseEntity<String>(jwtProvider.getUserPrincipal(token).getSapUserId(), HttpStatus.OK);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@GetMapping("/check/{token}/userId")
	public ResponseEntity<?> getUser(@PathVariable String token) throws InvalidJTWTokenException {
		if (permissions.validateJwtToken(token)) {
			try {
				return new ResponseEntity<Long>(jwtProvider.getUserPrincipal(token).getId(), HttpStatus.OK);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@GetMapping("/check/{token}/tenantId")
	public ResponseEntity<?> getTenant(@PathVariable String token) throws InvalidJTWTokenException {
		if (permissions.validateJwtToken(token)) {
			try {
				User user = userRepository.getOne(jwtProvider.getUserPrincipal(token).getId());
				return new ResponseEntity<Long>(user.getTenant().getId(), HttpStatus.OK);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@GetMapping("/getLogged")
	public ResponseEntity<?> getLogged() {

		if (userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent()) {
			User logged = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
					.get();
			return new ResponseEntity<UserDTO>(new UserDTO(logged), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Fail ->No logged user", HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/signout")
	public ResponseEntity<?> signout() {
		logger.info("LOGOUT");
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
		return null;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest)
			throws MessagingException {
		User logged = new User();
		if (userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent()) {
			logged = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		}
		if (userRepository.existsByEmailAndTenantId(signUpRequest.getEmail(), logged.getTenant().getId())) {
			return new ResponseEntity<>("Fail -> Email is already in use!", HttpStatus.CONFLICT);
		}

		if (signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
			try {
				Set<Role> roles = new HashSet<Role>();
				for (RoleDTO r : signUpRequest.getRoles()) {
					Role role = roleRepository.getOne(r.getId());
					roles.add(role);
				}
				User user = new User(signUpRequest, encoder.encode(signUpRequest.getPassword()), roles);

				user.setEnabled(false);

				user.setTenant(logged.getTenant());
				if (userRepository.existsBySapUserIdAndTenantId(signUpRequest.getSapUserId(),
						logged.getTenant().getId())) {
					return new ResponseEntity<>("Fail -> Sap id is already in use!", HttpStatus.NOT_ACCEPTABLE);
				}

				user = userRepository.save(user);
				UserDTO us = new UserDTO(user);

				mailNotification.sendEmail(signUpRequest.getEmail(), signUpRequest.getPassword());
				logger.info("user: " + user.getId() + " | R3USER | success");
				return new ResponseEntity<>(HttpStatus.CREATED);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return new ResponseEntity<>("Fail -> Passwords don't match!", HttpStatus.BAD_REQUEST);
		}
		return null;
	}

	@PutMapping("/updateUser")
	public ResponseEntity<?> updateUser(@RequestBody SignupRequestDTO request) throws MessagingException, IOException {
		User user = userRepository.getOne(request.getId());
		Set<Role> roles = new HashSet<Role>();
		for (RoleDTO r : request.getRoles()) {
			Role role = roleRepository.getOne(r.getId());
			roles.add(role);
		}
		user.setName(request.getName());
		user.setSurname(request.getSurname());
		user.setEmail(request.getEmail());
		user.setTelephone(request.getTelephone());
		user.setDateOfBirth(request.getDateOfBirth());
		user.setRoles(roles);
		user.setSapUserId(request.getSapUserId());
		if (request.getPassword() != null && request.getConfirmPassword() != null) {
			if (request.getPassword().equals(request.getConfirmPassword())) {
				user.setPassword(encoder.encode(request.getPassword()));
				userRepository.save(user);
				mailNotification.sendEmail(request.getEmail(), request.getPassword());
			}
		}

		userRepository.save(user);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("deleteUser/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		User user = userRepository.getOne(id);
		if (user != null) {
			for (Role r : user.getRoles()) {
				r.getUsers().remove(user);
				roleRepository.save(r);
			}
			userRepository.delete(user);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("activateUser/{id}")
	public ResponseEntity<?> activateUser(@PathVariable Long id) {
		User user = userRepository.getOne(id);
		if (user != null) {
			user.setEnabled(true);
			userRepository.save(user);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("deactivateUser/{id}")
	public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
		User user = userRepository.getOne(id);
		if (user != null) {
			user.setEnabled(false);
			userRepository.save(user);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("checkUserRoles/{id}")
	public ResponseEntity<?> checkUserRoles(@PathVariable Long id) {
		User user = userRepository.getOne(id);
		List<RoleDTO> roles = new ArrayList<RoleDTO>();
		for (Role r : user.getRoles()) {
			RoleDTO role = new RoleDTO(r);
			roles.add(role);
		}
		return new ResponseEntity<List<RoleDTO>>(roles, HttpStatus.OK);
	}

	@GetMapping("/getAdminRole")
	public ResponseEntity<?> getAdminRole() {

		if (userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent()) {
			User logged = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
					.get();
			for (Role r : logged.getRoles()) {
				if (r.getName().equals(RoleName.ROLE_SUPER_ADMIN)) {
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<>("Fail ->No logged user", HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/getUserSettings")
	public ResponseEntity<?> getUserSettings() {
		if (userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent()) {
			User logged = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
					.get();
			SettingDTO setting = new SettingDTO(logged.getTenant().getSetting());
			setting.setTenantId(logged.getTenant().getId());
			return new ResponseEntity<SettingDTO>(setting, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Fail ->No logged user", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/updateSettings/{tenantId}")
	public ResponseEntity<?> updateSettings(@PathVariable Long tenantId, @RequestBody SettingDTO settingDTO) {
		Tenant tenant = tenantRepository.getOne(tenantId);
		if (settingDTO.isUseSap() == true && settingDTO.getMasterDataFormat().equals("WITH_ID")) {
			long id = 1;
			Setting setting = settingRepository.getOne(id);
			tenant.setSetting(setting);
			tenantRepository.save(tenant);
		} else if (settingDTO.isUseSap() == true && settingDTO.getMasterDataFormat().equals("WITHOUT_ID")) {
			long id = 2;
			Setting setting = settingRepository.getOne(id);
			tenant.setSetting(setting);
			tenantRepository.save(tenant);
		} else {
			long id = 3;
			Setting setting = settingRepository.getOne(id);
			tenant.setSetting(setting);
			tenantRepository.save(tenant);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("getLoggedTenant")
	public ResponseEntity<?> getLoggedTenant() {
		if (userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent()) {
			User logged = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
					.get();
			return new ResponseEntity<Long>(logged.getTenant().getId(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Fail ->No logged user", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("requestResetPassword/{email}")
	public ResponseEntity<?> requestResetPassword(@PathVariable String email) throws MessagingException, IOException {
		User user = userRepository.findByEmail(email).orElse(null);
		if (user != null) {
			PasswordResetToken passToken = passwordResetRepo.findByUser(user.getId()).orElse(null);
			if (passToken != null) {
				passwordResetRepo.delete(passToken);
			}
			String token = UUID.randomUUID().toString();
			PasswordResetToken prt = new PasswordResetToken();
			prt.setToken(token);
			Date now = new Date();
			prt.setExpiryDate(new Date(now.getTime() + 10800000));
			prt.setUser(user);
			prt = passwordResetRepo.save(prt);
			mailNotification.sendEmailPasswordReset(user.getEmail(), token);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO rp) {
		PasswordResetToken prt = passwordResetRepo.findByToken(rp.getToken());
		Date now = new Date();
		if (prt != null && now.before(prt.getExpiryDate())) {
			User user = prt.getUser();
			user.setPassword(encoder.encode(rp.getPassword()));
			userRepository.save(user);
			passwordResetRepo.delete(prt);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}
