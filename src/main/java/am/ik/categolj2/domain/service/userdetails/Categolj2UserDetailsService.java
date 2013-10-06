package am.ik.categolj2.domain.service.userdetails;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.service.user.UserService;

public class Categolj2UserDetailsService implements UserDetailsService {
	@Inject
	protected UserService userService;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail)
			throws UsernameNotFoundException {
		try {
			User user = userService.findOneByUsernameOrEmail(usernameOrEmail);
			return new Categolj2UserDetails(user);
		} catch (ResourceNotFoundException e) {
			throw new UsernameNotFoundException(
					"The given username (or email) is not found. ["
							+ usernameOrEmail + "]", e);
		}
	}

}
