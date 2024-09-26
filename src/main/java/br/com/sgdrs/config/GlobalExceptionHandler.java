package br.com.sgdrs.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errorResponse = new HashMap<>();

        FieldError fieldError = (FieldError) exception.getBindingResult().getAllErrors().getFirst();
        String errorMessage = fieldError.getDefaultMessage();

        errorResponse.put("mensagem", errorMessage);
        errorResponse.put("status", String.valueOf(BAD_REQUEST.value()));
        errorResponse.put("erro", BAD_REQUEST.getReasonPhrase());

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        int httpStatusCode = exception.getStatusCode().value();

        errorResponse.put("mensagem", exception.getReason());
        errorResponse.put("status", String.valueOf(httpStatusCode));

        HttpStatus httpStatus = HttpStatus.resolve(httpStatusCode);
        if (httpStatus != null) {
            errorResponse.put("erro", httpStatus.getReasonPhrase());
        } else {
            errorResponse.put("erro", "Status desconhecido");
        }

        return new ResponseEntity<>(errorResponse, exception.getStatusCode());
    }

}
