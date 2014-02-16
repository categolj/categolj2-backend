package am.ik.categolj2.domain.service.accesslog;

import am.ik.categolj2.domain.model.AccessLog;
import org.springframework.scheduling.annotation.Async;

public interface AccessLogService {
    @Async
    void save(AccessLog accessLog);
}
