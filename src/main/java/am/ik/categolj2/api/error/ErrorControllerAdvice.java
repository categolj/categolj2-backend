package am.ik.categolj2.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

@ControllerAdvice
public class ErrorControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        return ErrorResponse.from(e, "invalid request!", e.getBindingResult().getFieldErrors());
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        logger.error("System Error", e);
        return ErrorResponse.from(e, "server error!", ResultMessages.error().add(ResultMessage.fromText(e.getMessage())));
    }
}
