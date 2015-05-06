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
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.net.ssl.SSLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping
public class AuthenticationController {
    private static Logger logger = LogManager.getLogger();

    @Inject
    RestTemplate restTemplate;
    @Inject
    ConsumerTokenServices tokenServices;
    @Inject
    AuthenticationHelper authenticationHelper;
    @Value("${server.port:8443}")
    int httpsPort;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    String loginForm() {
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    String login(@RequestParam("username") String username, @RequestParam("password") String password,
                 UriComponentsBuilder builder, RedirectAttributes attributes,
                 HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("attempt to login (username={})", username);
        String tokenEndpoint = builder.path("oauth/token").build().toUriString();
        HttpEntity<MultiValueMap<String, Object>> ropRequest = authenticationHelper.createRopRequest(username, password);
        try {
            ResponseEntity<OAuth2AccessToken> result = restTemplate.postForEntity(tokenEndpoint, ropRequest, OAuth2AccessToken.class);
            OAuth2AccessToken accessToken = result.getBody();
            authenticationHelper.saveAccessTokenInCookie(accessToken, response);
            authenticationHelper.writeLoginHistory(accessToken, request, response);
        } catch (HttpStatusCodeException e) {
            authenticationHelper.handleHttpStatusCodeException(e, attributes);
            return "redirect:/login";
        } catch (ResourceAccessException e) {
            // I/O error on POST request for "https://xxxx:8080/oauth/token":Unrecognized SSL message, plaintext connection?
            if (e.getCause() instanceof SSLException) {
                // fallback to another port
                UriComponentsBuilder b = builder.replacePath("").port(httpsPort);
                return login(username, password, b, attributes, request, response);
            } else {
                throw e;
            }
        }
        return "redirect:/admin";
    }

    @RequestMapping({"doLogout", "dologout"})
    String logout(@CookieValue(value = Categolj2Cookies.ACCESS_TOKEN_VALUE_COOKIE, required = false) String accessTokenValue,
                  @CookieValue(value = Categolj2Cookies.REFRESH_TOKEN_VALUE_COOKIE, required = false) String refreshTokenValue,
                  HttpServletResponse response) throws IOException {
        logger.debug("remove token {}", accessTokenValue);

        if (!Strings.isNullOrEmpty(refreshTokenValue)) {
            authenticationHelper.removeRefreshTokenFromCookie(response);
        }
        if (!Strings.isNullOrEmpty(accessTokenValue)) {
            authenticationHelper.removeAccessTokenFromCookie(response);
            tokenServices.revokeToken(accessTokenValue);
        }
        authenticationHelper.removeCookie("JSESSIONID", response);
        return "redirect:/login";
    }


}
