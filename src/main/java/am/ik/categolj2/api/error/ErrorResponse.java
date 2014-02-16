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

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ErrorResponse {
    private final String type;
    private final String message;
    private final List<ErrorResponseDetail> details = new ArrayList<>();

    public ErrorResponse add(String code, String message) {
        return add(code, message, null);
    }

    public ErrorResponse add(String code, String message, String target) {
        details.add(new ErrorResponseDetail(code, message, target));
        return this;
    }

    @Data
    @AllArgsConstructor
    static class ErrorResponseDetail {
        private final String code;
        private final String message;
        private final String target;
    }

    public static ErrorResponse from(ResultMessagesNotificationException e, String message) {
        return from(e, message, e.getResultMessages());
    }

    public static ErrorResponse from(Exception e, String message,
                                     List<FieldError> errors) {
        ErrorResponse response = ErrorResponse.of(e.getClass().getSimpleName(), message);
        for (FieldError error : errors) {
            response.add(null, error.getDefaultMessage(), error.getObjectName() + "." + error.getField());
        }
        return response;
    }

    public static ErrorResponse from(Exception e, String message,
                                     ResultMessages resultMessages) {
        ErrorResponse response = ErrorResponse.of(e.getClass().getSimpleName(), message);
        for (ResultMessage m : resultMessages) {
            response.add(m.getCode(), m.getText(), null);
        }
        return response;
    }
}
