package am.ik.categolj2.domain.repository.user;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Collections;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Collections2;

import am.ik.categolj2.domain.model.Role;
import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.repository.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@Transactional
public class UserRepositoryTest {
	DateTime now = new DateTime();
	@Inject
	UserRepository userRepository;

	@Before
	// @Rollback(false)
	public void setUp() {
		User user = new User("user1", "user1", "user1@example.com", true,
				false, "User1", "Name1", now, now, null, null, 0L, null);
		userRepository.save(user);
	}

	@Test
	// @Rollback(false)
	public void testSave() {
		User user = new User("making", "hoge", "makingx@gmail.com", true,
				false, "Toshiaki", "Maki", now, now, null, null, 0L, null);
		userRepository.save(user);
	}

	@Test
	public void testFindOne() {
		User user = userRepository.findOne("admin");
		System.out.println(user.getRoles());
		assertThat(user, is(notNullValue()));
	}
	

	@Test
	public void testFindAll() {
		System.out.println(userRepository.findAll());
	}
}
