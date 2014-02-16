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
import org.springframework.web.multipart.MultipartException;
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
        if (logger.isWarnEnabled()) {
            logger.warn("Business Error message={}", e.getMessage());
        }
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
        if (logger.isWarnEnabled()) {
            logger.warn("Access denied! method={},uri={},query={},principal={},remote={},user-agent={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    authentication == null ? "@@@@" : authentication.getName(),
                    RemoteAddresses.getRemoteAddress(request),
                    UserAgents.getUserAgent(request));
        }
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

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMultipartException(
            MultipartException e) {
        return ErrorResponse.from(e, "upload failed!",
                ResultMessages.error().add(ResultMessage.fromText(e.getMessage())));
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
