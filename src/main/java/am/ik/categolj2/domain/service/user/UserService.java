package am.ik.categolj2.domain.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import am.ik.categolj2.domain.model.User;

public interface UserService {
	User findOne(String username);

	User findOneByUsernameOrEmail(String usernameOrEmail);

	Page<User> findAllPage(Pageable pageable);

	User create(User user, String rawPassword);

	User update(User user, String rawPassword);

	void delete(String username);
}
