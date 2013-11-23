package am.ik.categolj2.domain.service.user;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.repository.role.RoleRepository;
import am.ik.categolj2.domain.repository.user.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Inject
	protected UserRepository userRepository;
	@Inject
	protected PasswordEncoder passwordEncoder;
	@Inject
	protected Mapper beanMapper;
	@Inject
	protected DateFactory dateFactory;
	@Inject
	protected RoleRepository roleRepository;

	@Override
	public User findOne(String username) {
		User user = userRepository.findDetails(username);
		if (user == null) {
			throw new ResourceNotFoundException("user is not found. [username="
					+ username + "]");
		}
		return user;
	}

	@Override
	public User findOneByUsernameOrEmail(String usernameOrEmail) {
		User user = userRepository.findOneByEmail(usernameOrEmail);
		if (user == null) {
			user = userRepository.findDetails(usernameOrEmail);
		}
		if (user == null) {
			throw new ResourceNotFoundException(
					"The given username (or email) is not found. [username="
							+ usernameOrEmail + "]");
		}
		return user;
	}

	@Override
	public Page<User> findAllPage(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Transactional
	@Override
	public User create(User user, String rawPassword) {
		Assert.notNull(user, "user must not be null");

		String username = user.getUsername();
		if (userRepository.exists(username)) {
			throw new BusinessException(
					"The given username is already used. [username=" + username
							+ "]");
		}

		String email = user.getEmail();
		if (userRepository.countByEmail(email) > 0) {
			throw new BusinessException(
					"The given email is already used. [email=" + email + "]");
		}

		// check roles
		checkRoles(user);

		String encodedPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encodedPassword);
		DateTime now = dateFactory.newDateTime();
		user.setCreatedDate(now);
		user.setLastModifiedDate(now);
		// createdBy,lastModifiedBy are set by AuditingEntityListener

		userRepository.save(user);
		return user;
	}

	@Transactional
	@Override
	public User update(String username, User updatedUser,
			String updatedRawPassword) {
		Assert.notNull(updatedUser, "user must not be null");

		String email = updatedUser.getEmail();

		// check the given user is existing
		if (!userRepository.exists(username)) {
			throw new BusinessException(
					"The given user does not exist. [username=" + username
							+ "]");
		}

		// check the given email is not used by others
		long countUsingEmail = userRepository.countByEmail(email);
		if (countUsingEmail > 0) {
			throw new BusinessException(
					"The given email is already used. [email=" + email + "]");
		}

		// check roles
		checkRoles(updatedUser);

		// encode raw password
		String encodedPassword = passwordEncoder.encode(updatedRawPassword);
		updatedUser.setPassword(encodedPassword);
		DateTime now = dateFactory.newDateTime();
		updatedUser.setLastModifiedDate(now);
		// lastModifiedBy are set by AuditingEntityListener

		User user = findOne(username);
		// copy new values to user
		beanMapper.map(updatedUser, user);
		userRepository.save(user);
		return user;
	}

	private void checkRoles(User user) {
		Set<Role> rolesToReplace = new HashSet<>();
		Set<Role> roles = user.getRoles();
		if (roles != null) {
			for (Role role : roles) {
				Integer roleId = role.getRoleId();
				Role loadedRole = roleRepository.findOne(roleId);
				if (loadedRole == null) {
					throw new BusinessException(
							"The given role is invalid. [roleId=" + roleId
									+ "]");
				}
				rolesToReplace.add(loadedRole);
			}
		}
		user.setRoles(rolesToReplace); // replace
	}

	@Transactional
	@Override
	public void delete(String username) {
		User user = findOne(username);
		userRepository.delete(user);
	}

}
