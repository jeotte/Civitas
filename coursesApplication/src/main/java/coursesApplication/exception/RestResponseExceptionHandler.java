package coursesApplication.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CourseValidationException.class)
	public final ResponseEntity<RestErrorDetails> handleValidationException(CourseValidationException exception,
			WebRequest request) {
		RestErrorDetails error = new RestErrorDetails(exception.getMessage(), request.getDescription(false),
				new Date());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
