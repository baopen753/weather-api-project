package com.baopen753.weatherapiproject.locationservices.error.global;


import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

@ControllerAdvice  // define a global exception handling
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    // private static final Logger LOGGER = LoggerFactory.getLogger(LocationNotFoundException.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(HttpServletRequest request, Exception exception) {
                                                                                                // to customize fields (default message) of Error HTTP response PROTOTYPE
        ErrorDTO error = new ErrorDTO();                                                        /*   this is a customized prototype of Error HTTP Response
                                                                                                    {
                                                                                                        "timeStamp": "2024-05-30T03:34:24.943+00:00",
                                                                                                        "status": 500,
                                                                                                        "path": null,
                                                                                                        "error": [
                                                                                                            "Internal Server Error"
                                                                                                                 ]
                                                                                                    }
                                                                                                */
        error.setTimeStamp(new Date());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setPath(request.getServletPath());

        LOGGER.error("This is a log message: " + exception.getMessage());

        return error;
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);

        ErrorDTO error = new ErrorDTO();

        error.setTimeStamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setPath(((ServletWebRequest) request).getRequest().getServletPath());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        final boolean[] isNull = {true};
        fieldErrors.forEach(fieldError -> {
            error.addError(fieldError.getDefaultMessage());
        });

        return new ResponseEntity<>(error, headers, status);
    }


}




















