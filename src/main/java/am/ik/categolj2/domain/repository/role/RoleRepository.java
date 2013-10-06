package am.ik.categolj2.domain.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;

import am.ik.categolj2.domain.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findOneByRoleName(String roleName);
}
