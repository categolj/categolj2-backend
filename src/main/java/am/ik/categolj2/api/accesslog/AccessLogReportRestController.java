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
package am.ik.categolj2.api.accesslog;

import am.ik.categolj2.App;
import am.ik.categolj2.api.Categolj2Headers;
import am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUri;
import am.ik.categolj2.domain.repository.accesslog.AccessLogReportGroupByUriAndRemoteAddress;
import am.ik.categolj2.domain.service.accesslog.AccessLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.terasoluna.gfw.common.query.QueryEscapeUtils;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("api/" + App.API_VERSION + "/accesslogreports")
public class AccessLogReportRestController {
    @Inject
    AccessLogService accessLogService;

    @RequestMapping(method = RequestMethod.GET, params = {"byUri", "uri"}, headers = Categolj2Headers.X_ADMIN)
    public List<AccessLogReportGroupByUri> getAccessLogReportsByUriLike(@RequestParam("uri") String uri) {
        String like = QueryEscapeUtils.toStartingWithCondition(uri);
        return accessLogService.findAllReportGroupByUriLike(like);
    }

    @RequestMapping(method = RequestMethod.GET, params = "byUri", headers = Categolj2Headers.X_ADMIN)
    public List<AccessLogReportGroupByUri> getAccessLogReportsByUri() {
        return accessLogService.findAllReportGroupByUri();
    }

    @RequestMapping(method = RequestMethod.GET, params = "byUriAndRemoteAddress", headers = Categolj2Headers.X_ADMIN)
    public List<AccessLogReportGroupByUriAndRemoteAddress> getAccessLogReportsByUriAndRemoteAddress() {
        return accessLogService.findAllReportGroupByUriAndRemoteAddress();
    }

    @RequestMapping(method = RequestMethod.DELETE, params = "remoteAddress", headers = Categolj2Headers.X_ADMIN)
    public long deleteAccessLogs(@RequestParam("remoteAddress") String remoteAddress) {
        return accessLogService.deleteFromRemoteAddress(remoteAddress);
    }
}
