//package br.com.sgdrs.config;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> erros = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String campo = ((FieldError) error).getField();
//            String mensagemErro = error.getDefaultMessage();
//            erros.put("message", mensagemErro);
//            erros.put("status", error.getCode());
//        });
//        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
//    }
//}
