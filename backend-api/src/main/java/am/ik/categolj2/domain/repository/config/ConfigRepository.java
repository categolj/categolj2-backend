package am.ik.categolj2.domain.repository.config;

import am.ik.categolj2.domain.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, String> {
}
