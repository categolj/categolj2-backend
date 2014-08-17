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
import am.ik.categolj2.domain.model.AccessLog;
import am.ik.categolj2.domain.service.accesslog.AccessLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("api/" + App.API_VERSION + "/accesslogs")
public class AccessLogRestController {
    @Inject
    AccessLogService accessLogService;

    @RequestMapping(method = RequestMethod.GET, headers = Categolj2Headers.X_ADMIN)
    public Page<AccessLog> getAccessLogs(@PageableDefault(size = 200) Pageable pageable) {
        return accessLogService.findPage(pageable);
    }

    @RequestMapping(method = RequestMethod.DELETE, params = "remoteAddress", headers = Categolj2Headers.X_ADMIN)
    public int deleteAccessLogs(@RequestParam("remoteAddress") String remoteAddress) {
        return accessLogService.deleteFromRemoteAddress(remoteAddress);
    }

    @RequestMapping(value = "{accessLogId}", method = RequestMethod.DELETE, headers = Categolj2Headers.X_ADMIN)
    public void deleteAccessLog(@PathVariable("accessLogId") String accessLogId) {
        accessLogService.delete(accessLogId);
    }
}
