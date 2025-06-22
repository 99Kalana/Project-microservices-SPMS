package lk.ijse.paymentservice.advice;

import lk.ijse.paymentservice.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Global exception handling for REST controllers
@CrossOrigin // Allows cross-origin requests (fine for development, specify origins for production)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.BAD_REQUEST.value(), // Consistent with HTTP status 400
                "Validation Error",
                errors
        );

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    // You can add more @ExceptionHandler methods here for other types of exceptions
    // For example, to handle general RuntimeExceptions or custom exceptions:
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO> handleRuntimeException(RuntimeException exception) {
        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500 status
                exception.getMessage(), // Return the exception message
                null
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}