package am.ik.categolj2.domain.service.accesslog;

import am.ik.categolj2.core.logger.LogManager;
import am.ik.categolj2.domain.model.AccessLog;
import am.ik.categolj2.domain.repository.accesslog.AccessLogRepository;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class AccessLogServiceImpl implements AccessLogService {
    private static Logger logger = LogManager.getLogger();
    @Inject
    AccessLogRepository accessLogRepository;

    @Override
    @Transactional
    public void save(AccessLog accessLog) {
        try {
            accessLogRepository.save(accessLog);
        } catch (DataAccessException e) {
            logger.error("storing access log failed. accessLog=" + accessLog, e);
        }
    }
}
