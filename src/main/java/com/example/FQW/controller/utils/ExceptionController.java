package com.example.FQW.controller.utils;

import com.example.FQW.controller.models.ErrorResponse;
import com.example.FQW.ex.RequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<?> handlerError(HttpServletRequest request, RequestException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorPath(request.getRequestURI());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> defaultException(HttpServletRequest request, RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorPath(request.getRequestURI());
        log.error("Request error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> notAuth(HttpServletRequest request, RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
