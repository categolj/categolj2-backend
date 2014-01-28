package am.ik.categolj2.domain.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import am.ik.categolj2.domain.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT DISTINCT x FROM User x JOIN FETCH x.roles WHERE x.username = :username")
    User findDetails(@Param("username") String username);

    @Query(value = "SELECT DISTINCT x FROM User x JOIN FETCH x.roles WHERE x.email = :email")
    User findOneByEmail(@Param("email") String email);

    @Query("SELECT x.username FROM User x WHERE x.email = :email")
    String findUsernameByEmail(@Param("email") String email);

    long countByEmail(String email);

    @Query("SELECT COUNT(x) FROM User x WHERE x.email = :email AND x.username != :username")
    long countByEmailOtherThanMe(@Param("email") String email, @Param("username") String username);
}
