package am.ik.categolj2.api.error;

import am.ik.categolj2.core.logger.LogManager;
import am.ik.categolj2.core.web.RemoteAddresses;
import am.ik.categolj2.core.web.UserAgents;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorControllerAdvice {
    private static final Logger logger = LogManager.getLogger();

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(
            ResourceNotFoundException e) {
        return ErrorResponse.from(e, "not found!");
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleBusinessException(
            BusinessException e) {
        logger.warn("Business Error", e);
        return ErrorResponse.from(e, "business error!");
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindException(
            BindException e) {
        return ErrorResponse.from(e, "invalid request!",
                e.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request, Authentication authentication) {
        logger.warn("Access denied! method={},uri={},query={},principal={},remote={},user-agent={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                authentication == null ? "@@@@" : authentication.getName(),
                RemoteAddresses.getRemoteAddress(request),
                UserAgents.getUserAgent(request));
        return ErrorResponse.from(e, "access denied!",
                ResultMessages.error().add(ResultMessage.fromText(e.getMessage())));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        return ErrorResponse.from(e, "invalid request!",
                e.getBindingResult().getFieldErrors());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        logger.error("System Error", e);
        return ErrorResponse.from(e, "server error!",
                ResultMessages.error().add(ResultMessage.fromText(e.getMessage())));
    }
}
