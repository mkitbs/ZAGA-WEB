package org.mkgroup.zaga.authorizationservice.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.mkgroup.zaga.authorizationservice.dto.SignupRequestDTO;
import org.mkgroup.zaga.authorizationservice.jwt.UserPrincipal;
import org.mkgroup.zaga.authorizationservice.model.Permission;
import org.mkgroup.zaga.authorizationservice.model.Role;
import org.mkgroup.zaga.authorizationservice.model.RoleName;
import org.mkgroup.zaga.authorizationservice.model.User;
import org.mkgroup.zaga.authorizationservice.repository.RoleRepository;
import org.mkgroup.zaga.authorizationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DomainUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger logger = Logger.getLogger(DomainUserDetailsService.class);

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		return getUserPrincipal(user);
	}
	
	public void changePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		logger.info("User " +  user.getEmail() + " has changed their password.");
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	public void changeUserNonLockStatusTrue(User user) {
		user.setNonLocked(true);
		userRepository.save(user);
	}

	public void changeUserNonLockStatusFalse(User user) {
		user.setNonLocked(false);
		userRepository.save(user);
	}

	public List<User> findAllUsers() {
		return userRepository.findUsersExceptSelf();
	}

	public User findCurrentUser() {
		return userRepository.findCurrentUser();
	}

	@Transactional
	public User registerUser(SignupRequestDTO signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return null;
		}

		User user = new User(signUpRequest.getEmail(), passwordEncoder.encode(signUpRequest.getPassword()),
				Collections.singleton(roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN)));

		userRepository.save(user);

		return user;
	}

	private UserPrincipal getUserPrincipal(User user) {
		Stream<String> roles = user.getRoles().stream()
				.map(Role::getName)
				.map(Enum::name);

		Stream<String> permissions = user.getRoles().stream()
				.map(Role::getPermissions)
				.flatMap(Collection::stream)
				.map(Permission::getName);

		List<GrantedAuthority> authorities = Stream.concat(roles, permissions)
				.distinct()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new UserPrincipal(user.getId(), user.getPassword(), user.getEmail(), user.isEnabled(), authorities,
				user.isNonLocked(), user.getSapUserId());
	}

	public void activateUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);
	}

	public List<String> getUserAuthorities(User user) {
		Stream<String> roles = user.getRoles().stream()
				.map(Role::getName)
				.map(Enum::name);

		Stream<String> permissions = user.getRoles().stream()
				.map(Role::getPermissions)
				.flatMap(Collection::stream)
				.map(Permission::getName);

		List<GrantedAuthority> authorities = Stream.concat(roles, permissions)
				.distinct()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		List<String> auts = new ArrayList<String>();
		for (GrantedAuthority nesto : authorities) {
			auts.add(nesto.getAuthority());
		}

		return auts;
	}

}
