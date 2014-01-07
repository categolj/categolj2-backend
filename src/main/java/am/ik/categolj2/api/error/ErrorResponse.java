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
    private final Class<?> type;
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
        ErrorResponse response = ErrorResponse.of(e.getClass(), message);
        for (FieldError error : errors) {
            response.add(null, error.getDefaultMessage(), error.getObjectName() + "." + error.getField());
        }
        return response;
    }

    public static ErrorResponse from(Exception e, String message,
                                     ResultMessages resultMessages) {
        ErrorResponse response = ErrorResponse.of(e.getClass(), message);
        for (ResultMessage m : resultMessages) {
            response.add(m.getCode(), m.getText(), null);
        }
        return response;
    }
}
