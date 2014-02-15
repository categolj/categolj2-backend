package am.ik.categolj2.core.web;

import com.google.common.net.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public class RemoteAddresses {

    public static String getRemoteAddress(HttpServletRequest request) {
        String forwardedFor = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        if (forwardedFor != null) {
            return forwardedFor;
        }
        return request.getRemoteAddr();
    }
}
