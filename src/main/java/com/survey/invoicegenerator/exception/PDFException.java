package com.survey.invoicegenerator.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PDFException extends RuntimeException{

    public PDFException(String message) {

        super(message);
    }
}
