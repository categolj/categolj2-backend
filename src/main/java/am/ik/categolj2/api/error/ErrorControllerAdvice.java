package am.ik.categolj2.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

@ControllerAdvice
public class ErrorControllerAdvice {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorResponse handleResourceNotFoundException(
			ResourceNotFoundException e) {
		return ErrorResponse.from("not found", e);
	}
}
