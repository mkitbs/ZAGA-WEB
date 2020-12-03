package org.mkgroup.zaga.authorizationservice.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String password;
	private String username;
	private boolean enabled;
	private boolean nonLocked;
	private String sapUserId;
	private Collection<? extends GrantedAuthority> autorities;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public UserPrincipal(Long id, String password, String username, boolean enabled, Collection<? extends GrantedAuthority> autorities, boolean locked, String sapUserId) {
		this.id = id;
		this.password = password;
		this.username = username;
		this.autorities = autorities;
		this.enabled = enabled;
		this.nonLocked = locked;
		this.sapUserId = sapUserId;
	}
	
	public UserPrincipal(Long id, String username, boolean enabled, Collection<? extends GrantedAuthority> autorities, boolean locked, String sapUserId) {
		this(id, null, username, enabled, autorities, locked, sapUserId);
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return autorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return nonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public String getSapUserId() {
		return sapUserId;
	}

	
}
