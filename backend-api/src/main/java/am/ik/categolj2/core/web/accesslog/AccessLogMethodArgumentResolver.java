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
package am.ik.categolj2.core.web.accesslog;


import am.ik.categolj2.core.web.RemoteAddresses;
import am.ik.categolj2.core.web.UserAgents;
import am.ik.categolj2.domain.model.AccessLog;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.terasoluna.gfw.common.date.DateFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class AccessLogMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Inject
    DateFactory dateFactory;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AccessLog.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String xTrack = StringUtils.substring((String) request.getAttribute("X-Track"), 0, 32);
        String method = request.getMethod();
        String uri = StringUtils.substring(request.getRequestURI(), 0, 128);
        String query = StringUtils.substring(request.getQueryString(), 0, 128);
        String remoteAddress = RemoteAddresses.getRemoteAddress(request);
        String userAgent = UserAgents.getUserAgent(request);
        DateTime accessDate = dateFactory.newDateTime();
        return new AccessLog(null, method, uri, query, remoteAddress, userAgent, xTrack, accessDate);
    }
}
