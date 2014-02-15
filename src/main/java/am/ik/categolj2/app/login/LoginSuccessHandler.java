package am.ik.categolj2.app.login;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import am.ik.categolj2.core.web.RemoteAddresses;
import am.ik.categolj2.core.web.UserAgents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.terasoluna.gfw.common.date.DateFactory;

import com.google.common.net.HttpHeaders;

import am.ik.categolj2.domain.model.LoginHistory;
import am.ik.categolj2.domain.service.loginhistory.LoginHistoryService;

public class LoginSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory
            .getLogger(LoginSuccessHandler.class);

    @Inject
    LoginHistoryService loginHistoryService;
    @Inject
    DateFactory dateFactory;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        logger.info("Login {}", authentication);
        LoginHistory loginHistory = createHistory(request,
                authentication.getName());
        loginHistoryService.save(loginHistory);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    LoginHistory createHistory(HttpServletRequest request, String username) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setLoginAgent(UserAgents.getUserAgent(request));
        loginHistory.setLoginHost(RemoteAddresses.getRemoteAddress(request));
        loginHistory.setLoginDate(dateFactory.newDateTime());
        loginHistory.setUsername(username);
        return loginHistory;
    }
}
