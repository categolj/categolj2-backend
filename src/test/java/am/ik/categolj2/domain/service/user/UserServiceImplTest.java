package am.ik.categolj2.domain.service.user;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class UserServiceImplTest {
	@Inject
	UserService userService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindOne() {
		User user = userService.findOne("admin");
		assertThat(user, is(notNullValue()));
	}

	@Test
	public void testFindOneByUsernameOrEmail_username() {
		User user = userService.findOneByUsernameOrEmail("admin");
		assertThat(user, is(notNullValue()));
	}

	@Test
	public void testFindOneByUsernameOrEmail_email() {
		User user = userService.findOneByUsernameOrEmail("admin@example.com");
		assertThat(user, is(notNullValue()));
	}

	@Test(expected = ResourceNotFoundException.class)
	public void testFindOneByUsernameOrEmail_notFound() {
		userService.findOneByUsernameOrEmail("not_found@example.com");
	}

	@Test
	public void testFindAllPage() {
	}

	@Test
	@Rollback(false)
	@Transactional
	public void testCreate() {
		User user = new User("testuser", null, "testuser@example.com", true,
				false, "Test", "User", new HashSet<Role>());
		user.getRoles().add(new Role(2));
		userService.create(user, "password");
	}

	@Test(expected = BusinessException.class)
	@Transactional
	public void testCreate_already_exist() {
		User user = new User("admin", null, "testuser@example.com", true,
				false, "Test", "User", new HashSet<Role>());
		user.getRoles().add(new Role(2));
		userService.create(user, "password");
	}

	@Test(expected = BusinessException.class)
	@Transactional
	public void testCreate_email_is_used() {
		User user = new User("testuser2", null, "admin@example.com", true,
				false, "Test", "User", null);
		user.setRoles(Collections.singleton(new Role(2)));
		userService.create(user, "password");
	}

	@Test(expected = BusinessException.class)
	@Transactional
	public void testCreate_illeagal_role() {
		User user = new User("testuser2", null, "testuser2@example.com", true,
				false, "Test", "User", null);
		user.setRoles(Collections.singleton(new Role(100)));
		userService.create(user, "password");
	}

	@Test
	@Rollback(false)
	@Transactional
	public void testUpdate() {
		System.out.println("test update");
		User user = new User("testuser", null, "testuser@example.com", true,
				false, "Test", "User", new HashSet<Role>()).lock().disable();
		user.getRoles().clear();
		user.getRoles().add(new Role(1));
		System.out.println("lock");
		user.setEmail("testuser2@example.com");
		userService.update("testuser", user, "password");
	}

	@Test(expected = BusinessException.class)
	@Transactional
	public void testUpdate_email_is_already_used() {
		System.out.println("hoge");
		User user = new User("testuser", null, "testuser@example.com", true,
				false, "Test", "User", null);
		user.setEmail("admin@example.com");
		userService.update("testuser", user, "password");
	}

	@Test(expected = BusinessException.class)
	@Transactional
	public void testUpdate_not_existuser() {
		User user = new User("not_exist_user", null,
				"not_exist_user@example.com", true, false, "Test", "User", null);
		user.setEmail("not_exist_user@example.com");
		userService.update("not_exist_user", user, "password");
	}

	@Test(expected = BusinessException.class)
	@Transactional
	public void testUpdate_illegal_role() {
		System.out.println("test update illegal role");
		User user = new User("testuser", null, "testuser@example.com", true,
				false, "Test", "User", new HashSet<Role>());
		user.getRoles().add(new Role(100));
		userService.update("testuser", user, "password");
	}

	@Test
	@Rollback(false)
	@Transactional
	public void testDelete() {
		userService.delete("testuser");
	}

}
