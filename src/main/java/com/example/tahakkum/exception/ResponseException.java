package com.example.tahakkum.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.HttpStatus;

public class ResponseException extends Exception {
    public class ErrorObject{
        String message;
        Optional<String> detail;

        public ErrorObject(String msg, Optional<String> d){
            message = msg;
            detail = d;
        }

        @Override
        public String toString(){
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("message", message);
            temp.put("detail", detail.toString());
            return temp.toString();
        }
    }

    public ArrayList<ErrorObject> errors;
    public HttpStatus statusCode;

    public ResponseException(String msg, HttpStatus status){
        super(msg);
        statusCode = status;
        errors = new ArrayList<ErrorObject>();
        errors.add(new ErrorObject(msg, Optional.empty()));
    }

    public ResponseException(String msg){
        super(msg);
        statusCode = HttpStatus.BAD_REQUEST;
        errors = new ArrayList<ErrorObject>();
        errors.add(new ErrorObject(msg, Optional.empty()));
    }

    // @Override
    // public String toString(){
    //     return errors.fo
    // }
}
