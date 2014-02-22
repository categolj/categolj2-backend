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
package am.ik.categolj2.domain.repository.accesslog;

import am.ik.categolj2.domain.model.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, String> {
    @Query("SELECT x FROM AccessLog x ORDER BY x.accessDate DESC")
    Page<AccessLog> findPageOrderByAccessDateDesc(Pageable pageable);

    @Query("SELECT NEW am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUri(x.uri, COUNT(x.uri))" +
            " FROM AccessLog x GROUP BY x.uri HAVING x.uri LIKE :uri ORDER BY COUNT(x.uri) DESC")
    List<AccessLogReportGroupByUri> findAllReportGroupByUriLike(@Param("uri") String uri);

    @Query("SELECT NEW am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUri(x.uri, COUNT(x.uri))" +
            " FROM AccessLog x GROUP BY x.uri ORDER BY COUNT(x.uri) DESC")
    List<AccessLogReportGroupByUri> findAllReportGroupByUri();

    @Query("SELECT NEW am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUriAndRemoteAddress(x.uri, x.remoteAddress, COUNT(x.remoteAddress))" +
            " FROM AccessLog x GROUP BY x.uri, x.remoteAddress ORDER BY x.uri, COUNT(x.uri) DESC")
    List<AccessLogReportGroupByUriAndRemoteAddress> findAllReportGroupByUriAndRemoteAddress();

    @Query("DELETE FROM AccessLog x WHERE x.remoteAddress = :remoteAddress")
    @Modifying
    int deleteFromRemoteAddress(@Param("remoteAddress") String remoteAddress);
}
