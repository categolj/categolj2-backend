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

import am.ik.categolj2.domain.Categolj2AuthorizeAccesses;
import am.ik.categolj2.domain.model.AccessLog;
import am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUri;
import am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUriAndRemoteAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface AccessLogService {
    @Async
    void save(AccessLog accessLog);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    Page<AccessLog> findPage(Pageable pageable);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    List<AccessLogReportGroupByUri> findAllReportGroupByUriLike(String uri);

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    List<AccessLogReportGroupByUri> findAllReportGroupByUri();

    @PreAuthorize(Categolj2AuthorizeAccesses.AUTHORIZED)
    List<AccessLogReportGroupByUriAndRemoteAddress> findAllReportGroupByUriAndRemoteAddress();

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    int deleteFromRemoteAddress(String remoteAddress);

    @PreAuthorize(Categolj2AuthorizeAccesses.ADMIN_ONLY)
    void delete(String accessLogId);
}
