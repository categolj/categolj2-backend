package am.ik.categolj2.core.web;

import com.google.common.net.HttpHeaders;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class RemoteAddresses {

    public static String getRemoteAddress(HttpServletRequest request) {
        String forwardedFor = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        String result;
        if (forwardedFor != null) {
            result = forwardedFor;
        } else {
            result = request.getRemoteAddr();
        }
        return StringUtils.substring(result, 0, 128);
    }
}
