/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.domain.service.accesslog;

import am.ik.categolj2.core.logger.LogManager;
import am.ik.categolj2.domain.model.AccessLog;
import am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUri;
import am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUriAndRemoteAddress;
import am.ik.categolj2.domain.repository.accesslog.AccessLogRepository;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

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

    @Override
    public Page<AccessLog> findPage(Pageable pageable) {
        return accessLogRepository.findPageOrderByAccessDateDesc(pageable);
    }

    @Override
    public List<AccessLogReportGroupByUri> findAllReportGroupByUriLike(String uri) {
        return accessLogRepository.findAllReportGroupByUriLike(uri);
    }

    @Override
    public List<AccessLogReportGroupByUri> findAllReportGroupByUri() {
        return accessLogRepository.findAllReportGroupByUri();
    }

    @Override
    public List<AccessLogReportGroupByUriAndRemoteAddress> findAllReportGroupByUriAndRemoteAddress() {
        return accessLogRepository.findAllReportGroupByUriAndRemoteAddress();
    }

    @Transactional
    @Override
    public int deleteFromRemoteAddress(String remoteAddress) {
        return accessLogRepository.deleteFromRemoteAddress(remoteAddress);
    }

    @Transactional
    @Override
    public void delete(String accessLogId) {
        accessLogRepository.delete(accessLogId);
    }
}
