package app.auth.service.Error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
//        ApiError error = new ApiError();
//        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        error.setMessage(ex.getMessage());
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
//        ApiError error = new ApiError();
//        error.setMessage("Something went wrong "+ex.getMessage());
//        error.setStatusCode( HttpStatus.INTERNAL_SERVER_ERROR.value());
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//}