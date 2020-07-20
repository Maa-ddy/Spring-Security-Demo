package maaddy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private Collection<? extends GrantedAuthority> authorities;
	private String username;
	private String password;
	private String email;
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	
	public UserDetailsImpl(Person user) {
		this.username = user.getName();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.enabled = user.isActive();
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Authority role: user.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			System.out.println(role.getName());
		}
		this.authorities = authorities;
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
	}

}
