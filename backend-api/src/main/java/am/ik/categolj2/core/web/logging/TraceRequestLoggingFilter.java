package am.ik.categolj2.core.web.logging;


import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TraceRequestLoggingFilter extends AbstractRequestLoggingFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (logger.isTraceEnabled()) {
            boolean isFirstRequest = !isAsyncDispatch(request);
            if (isFirstRequest) {
                beforeRequest(request, createMessage(request, "Before[method=" + request.getMethod() + ";", "]"));
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (logger.isTraceEnabled()) {
                if (!isAsyncStarted(request)) {
                    afterRequest(request, createMessage(request, "After [method=" + request.getMethod() + ";", "]"));
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
