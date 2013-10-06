package am.ik.categolj2.domain.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import am.ik.categolj2.domain.model.User;

public interface UserRepository extends JpaRepository<User, String> {
	User findOneByEmail(String email);

	@Query("SELECT x.username FROM User x WHERE x.email = :email")
	String findUsernameByEmail(@Param("email") String email);

	long countByEmail(String email);
}
