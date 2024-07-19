package com.example.tahakkum.handler;

import java.util.HashMap;
import java.util.StringJoiner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.tahakkum.exception.ResponseException;
import com.example.tahakkum.exception.ResponseException.ErrorObject;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleInvalidRequestException(MissingServletRequestParameterException e) {
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("errors", e.getMessage());
        res.put("statusCode", "400");
        System.out.println(res);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> errorMsg = new HashMap<String, String>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName;
            try {
                fieldName = ((FieldError) error).getField();

            } catch (ClassCastException ex) {
                fieldName = error.getObjectName();
            }
            String message = error.getDefaultMessage();
            errorMsg.put(fieldName, message);
        });
        return new ResponseEntity<>(errorMsg,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<Object> handleResponseException(ResponseException e) {
        HashMap<String, String> res = new HashMap<String, String>();
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (ErrorObject s : e.errors) {
            joiner.add(s.toString());
        }

        res.put("errors", joiner.toString());
        res.put("statusCode", Integer.toString( e.statusCode.value()));
        System.out.println(res);
        return new ResponseEntity<>(res, e.statusCode);
    }
}
