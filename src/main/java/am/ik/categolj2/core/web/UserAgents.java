package am.ik.categolj2.core.web;

import com.google.common.net.HttpHeaders;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class UserAgents {
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = StringUtils.substring(request.getHeader(HttpHeaders.USER_AGENT), 0, 128);
        return userAgent;
    }
}
