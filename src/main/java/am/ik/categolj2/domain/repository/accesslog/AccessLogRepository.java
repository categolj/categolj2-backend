package am.ik.categolj2.domain.repository.accesslog;

import am.ik.categolj2.domain.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, String> {
}
