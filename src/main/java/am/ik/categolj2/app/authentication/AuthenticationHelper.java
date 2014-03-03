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
package am.ik.categolj2.app.authentication;

import am.ik.categolj2.app.Categolj2Cookies;
import am.ik.categolj2.core.logger.LogManager;
import am.ik.categolj2.core.web.RemoteAddresses;
import am.ik.categolj2.core.web.UserAgents;
import am.ik.categolj2.domain.model.LoginHistory;
import am.ik.categolj2.domain.service.loginhistory.LoginHistoryService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.date.DateFactory;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class AuthenticationHelper {
    private static Logger logger = LogManager.getLogger();

    @Inject
    LoginHistoryService loginHistoryService;
    @Inject
    DateFactory dateFactory;
    @Inject
    ObjectMapper objectMapper;

    HttpEntity<MultiValueMap<String, Object>> createRopRequest(String username, String password) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", password);
        params.add("client_id", "categolj2-admin");
        params.add("client_secret", "categolj2-secret");
        params.add("grant_type", "password");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(params, headers);
    }

    void saveAccessTokenInCookie(OAuth2AccessToken accessToken, HttpServletResponse response) throws UnsupportedEncodingException {
        Cookie accessTokenValueCookie = new Cookie(Categolj2Cookies.ACCESS_TOKEN_VALUE_COOKIE,
                URLEncoder.encode(accessToken.getValue(), "UTF-8"));
        accessTokenValueCookie.setMaxAge(accessToken.getExpiresIn());
        Cookie accessTokenExpireCookie = new Cookie(Categolj2Cookies.ACCESS_TOKEN_EXPIRATION_COOKIE,
                URLEncoder.encode(String.valueOf(accessToken.getExpiration().getTime()), "UTF-8"));
        accessTokenExpireCookie.setMaxAge(accessToken.getExpiresIn());

        response.addCookie(accessTokenValueCookie);
        response.addCookie(accessTokenExpireCookie);

        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            Cookie refreshTokenCookie = new Cookie(Categolj2Cookies.REFRESH_TOKEN_VALUE_COOKIE,
                    URLEncoder.encode(refreshToken.getValue(), "UTF-8"));
            response.addCookie(refreshTokenCookie);
        }
    }

    void removeCookie(String cookieName, HttpServletResponse response) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie(cookieName,
                URLEncoder.encode("", "UTF-8"));
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    void removeAccessTokenFromCookie(HttpServletResponse response) throws UnsupportedEncodingException {
        removeCookie(Categolj2Cookies.ACCESS_TOKEN_VALUE_COOKIE, response);
        removeCookie(Categolj2Cookies.ACCESS_TOKEN_EXPIRATION_COOKIE, response);
    }

    void removeRefreshTokenFromCookie(HttpServletResponse response) throws UnsupportedEncodingException {
        removeCookie(Categolj2Cookies.REFRESH_TOKEN_VALUE_COOKIE, response);
    }

    void writeLoginHistory(OAuth2AccessToken accessToken, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // user
        Map<String, ?> user = (Map<String, ?>) accessToken.getAdditionalInformation().get("user");
        if (user != null) {
            String username = (String) user.get("username");
            String firstName = (String) user.get("firstName");
            String lastName = (String) user.get("lastName");
            String email = (String) user.get("email");

            LoginHistory loginHistory = createHistory(username, request);
            loginHistoryService.save(loginHistory);

            saveUserInformationInCookie(username, firstName, lastName, email, response);
        } else {
            logger.error("No user information! (access_token={})", accessToken);
        }
    }

    void saveUserInformationInCookie(String username, String firstName, String lastName, String email,
                                     HttpServletResponse response) throws UnsupportedEncodingException {
        String json = String.format("{\"username\":\"%s\"," +
                "\"firstName\":\"%s\"," +
                "\"lastName\":\"%s\"," +
                "\"email\":\"%s\"}", username, firstName, lastName, email);
        Cookie cookie = new Cookie(Categolj2Cookies.USER_COOKIE,
                URLEncoder.encode(json, "UTF-8"));
        response.addCookie(cookie);
    }


    LoginHistory createHistory(String username, HttpServletRequest request) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setLoginAgent(UserAgents.getUserAgent(request));
        loginHistory.setLoginHost(RemoteAddresses.getRemoteAddress(request));
        loginHistory.setLoginDate(dateFactory.newDateTime());
        loginHistory.setUsername(username);
        return loginHistory;
    }

    void handleHttpStatusCodeException(HttpStatusCodeException e, RedirectAttributes attributes) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("authentication failed (message={},X-Track={})",
                    e.getMessage(),
                    e.getResponseHeaders().get("X-Track"));
        }
        OAuth2Exception oAuth2Exception = objectMapper.readValue(e.getResponseBodyAsByteArray(),
                OAuth2Exception.class);
        attributes.addAttribute("error", oAuth2Exception.getMessage());
    }


}
