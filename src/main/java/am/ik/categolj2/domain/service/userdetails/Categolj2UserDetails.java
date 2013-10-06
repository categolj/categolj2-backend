package am.ik.categolj2.domain.service.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;

public class Categolj2UserDetails extends
		org.springframework.security.core.userdetails.User {
	private static final long serialVersionUID = 1L;

	private final User user;

	public Categolj2UserDetails(User user) {
		super(user.getUsername(), user.getPassword(), user.isEnabled(), false,
				false, !user.isLocked(), toAuthorities(user.getRoles()));
		this.user = user;
	}

	private static Collection<? extends GrantedAuthority> toAuthorities(
			Collection<Role> roles) {
		return Collections2.transform(roles,
				new Function<Role, SimpleGrantedAuthority>() {
					@Override
					public SimpleGrantedAuthority apply(Role role) {
						return new SimpleGrantedAuthority("ROLE_"
								+ role.getRoleName());
					}
				});
	}

	public User getUser() {
		return user;
	}

}
