package com.example.kyrsovay.controller.utils;

import com.example.kyrsovay.controller.models.ErrorResponse;
import com.example.kyrsovay.ex.RequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<?> handlerError(RequestException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }
}
