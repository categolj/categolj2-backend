package am.ik.categolj2.api.error;

import java.util.ArrayList;
import java.util.List;

import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "message")
public class ErrorResponse {
	private final String message;
	private final List<ErrorResponseDetail> details = new ArrayList<>();

	public ErrorResponse add(String code, String message) {
		details.add(new ErrorResponseDetail(code, message));
		return this;
	}

	@Data
	@AllArgsConstructor
	static class ErrorResponseDetail {
		private final String code;
		private final String message;
	}

	public static ErrorResponse from(String message,
			ResultMessagesNotificationException e) {
		ErrorResponse response = ErrorResponse.message(message);
		ResultMessages messages = e.getResultMessages();
		for (ResultMessage m : messages) {
			response.add(m.getCode(), m.getText());
		}
		return response;
	}
}
