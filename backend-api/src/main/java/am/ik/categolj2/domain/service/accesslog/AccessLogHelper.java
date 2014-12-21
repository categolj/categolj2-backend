package am.ik.categolj2.domain.service.accesslog;


import am.ik.categolj2.domain.model.AccessLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class AccessLogHelper {
    @Value("${accesslog.disabled:false}")
    boolean isAccessLogDisabled;
    @Inject
    AccessLogService accessLogService;

    public void writeIfAccessLogIsEnabled(AccessLog accessLog) {
        if (!isAccessLogDisabled) {
            accessLogService.save(accessLog);
        }
    }
}
