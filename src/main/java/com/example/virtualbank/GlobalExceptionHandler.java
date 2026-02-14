package com.example.virtualbank;

import com.example.virtualbank.exceptions.EntityAlreadyExistsException;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.exceptions.UnauthorizedException;
import com.example.virtualbank.responsebody.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ResponseBody<String>> handleAlreadyExists(EntityAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseBody.responseBody(HttpStatus.BAD_REQUEST.value(), exception.getMessage())
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseBody<String>> handleNotFound(EntityNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseBody.responseBody(HttpStatus.NOT_FOUND.value(), exception.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBody<String>> handleValid(MethodArgumentNotValidException exception){
        String errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                  ResponseBody.responseBody(HttpStatus.BAD_REQUEST.value(), errors)
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseBody<String>> handleUnauthorized(UnauthorizedException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseBody.responseBody(HttpStatus.BAD_REQUEST.value(), exception.getMessage())
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseBody<String>> handleNullPointer(NullPointerException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseBody.responseBody(HttpStatus.BAD_REQUEST.value(), exception.getMessage())
        );
    }
}
