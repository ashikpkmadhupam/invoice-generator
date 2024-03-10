package com.survey.invoicegenerator.exception.handler;

import com.survey.invoicegenerator.exception.PDFException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class PDFExceptionHandler {

    @ExceptionHandler(PDFException.class)
    public ResponseEntity<Object> handlePDFException(PDFException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
