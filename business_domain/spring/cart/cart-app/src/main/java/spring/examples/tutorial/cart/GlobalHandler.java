package spring.examples.tutorial.cart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spring.examples.tutorial.cart.common.BookException;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(BookException.class)
    public ResponseEntity<String> handleBookException(BookException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(ex.getMessage(), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ex.getMessage(), status);
    }

}
