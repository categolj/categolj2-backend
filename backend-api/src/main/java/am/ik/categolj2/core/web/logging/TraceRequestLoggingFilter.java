package am.ik.categolj2.core.web.logging;


import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TraceRequestLoggingFilter extends AbstractRequestLoggingFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest requestToUse = request;
        if (logger.isTraceEnabled()) {
            boolean isFirstRequest = !isAsyncDispatch(request);
            if (isFirstRequest) {
                beforeRequest(request, createMessage(request, "Before[method=" + request.getMethod() + ";", "]"));
            }
            if(this.isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
                requestToUse = new ContentCachingRequestWrapper(request);
            }
        }

        try {
            filterChain.doFilter(requestToUse, response);
        } finally {
            if (logger.isTraceEnabled()) {
                if (!isAsyncStarted(requestToUse)) {
                    afterRequest(requestToUse, createMessage(requestToUse, "After [method=" + requestToUse.getMethod() + ";", "]"));
                }
            }
        }
    }


    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.trace(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.trace(message);
    }
}
