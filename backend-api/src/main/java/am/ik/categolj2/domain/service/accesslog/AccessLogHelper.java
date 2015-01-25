package am.ik.categolj2.domain.service.accesslog;


import am.ik.categolj2.domain.model.AccessLog;
import is.tagomor.woothee.Classifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class AccessLogHelper {
    @Value("${accesslog.disabled:false}")
    boolean isAccessLogDisabled;
    @Value("${accesslog.ignore.bot:true}")
    boolean ignoreCrawler;
    @Inject
    AccessLogService accessLogService;

    public void writeIfAccessLogIsEnabled(AccessLog accessLog) {
        if (!isAccessLogDisabled && !(ignoreCrawler && Classifier.isCrawler(accessLog.getUserAgent()))) {
            accessLogService.save(accessLog);
        }
    }
}
