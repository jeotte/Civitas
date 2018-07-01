package coursesApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CourseValidationException extends Exception {

	private static final long serialVersionUID = -3342545062236532302L;

	public CourseValidationException(ExceptionMsg message) {
		super(message.toString());
	}

	public CourseValidationException(String className, ExceptionMsg message) {
		super(className + ": " + message.toString());
	}

	public CourseValidationException(String className, ExceptionMsg message, Throwable throwable) {
		super(className + ": " + message.toString(), throwable);
	}
}
