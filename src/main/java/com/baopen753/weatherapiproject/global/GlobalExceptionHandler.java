package com.baopen753.weatherapiproject.global;


import com.baopen753.weatherapiproject.GeolocationException;
import com.baopen753.weatherapiproject.hourlyweatherservices.exception.BadRequestException;
import com.baopen753.weatherapiproject.locationservices.exception.LocationExistedException;
import com.baopen753.weatherapiproject.locationservices.exception.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
import java.util.Set;

@ControllerAdvice  // define a global exception handling
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // private static final Logger LOGGER = LoggerFactory.getLogger(LocationNotFoundException.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);



    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(HttpServletRequest request, Exception exception) {

        LOGGER.error("This is a log message: " + exception.getMessage());

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


        return error;
    }

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleLocationNotFoundException(HttpServletRequest request, Exception exception) {
        // Logger the error message
        LOGGER.error("This is a log message : " + exception.getMessage());

        // create Error object
        ErrorDTO errorDTO = new ErrorDTO();

        // set Error object by customized error
        errorDTO.setTimeStamp(new Date());
        errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
        errorDTO.addError(exception.getMessage());
        errorDTO.setPath(request.getServletPath());

        return errorDTO;
    }

    @ExceptionHandler(LocationExistedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO handleLocationExistedException(HttpServletRequest request, Exception exception) {
        // Logger the error message
        LOGGER.error("This is log message: " + exception.getMessage());

        // Create Error object
        ErrorDTO errorDTO = new ErrorDTO();

        // Set error object by customized error
        errorDTO.setTimeStamp(new Date());
        errorDTO.setStatus(HttpStatus.CONFLICT.value());
        errorDTO.addError(exception.getMessage());
        errorDTO.setPath(request.getServletPath());

        return errorDTO;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handlerConstraintViolationException(HttpServletRequest request, Exception exception) {
        // Logger the message to console
        LOGGER.error("This is log message: " + exception.getMessage() + " from handlerConstraintViolationException");

        // create Error object
        ErrorDTO errorDTO = new ErrorDTO();

        // customize Error object
        errorDTO.setTimeStamp(new Date());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(request.getServletPath());

        if (exception instanceof ConstraintViolationException) {
            int count = 0;
            ConstraintViolationException cve = (ConstraintViolationException) exception;
            Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                errorDTO.addError(constraintViolation.getMessage());
            }
        }
        else
            errorDTO.addError(exception.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(GeolocationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleGeolocationException(HttpServletRequest request, Exception exception) {
        LOGGER.error("This is log message: " + exception.getMessage());

        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setTimeStamp(new Date());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(request.getServletPath());
        errorDTO.addError(exception.getMessage());

        return errorDTO;
    }

    @ExceptionHandler(HttpResponseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleRequestLackingOfXCurrentHourHeader(HttpServletRequest request, Exception exception) {
        LOGGER.error("This is log message: " + exception.getMessage());

        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setTimeStamp(new Date());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(request.getServletPath());
        errorDTO.addError(exception.getMessage());

        return errorDTO;
    }


    @ExceptionHandler(BadRequestException.class)     // handle when updating hourlyweather without body list request
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleRequestEmptyListBodyWithBranches(HttpServletRequest request, Exception exception) {
        LOGGER.error("This is log message: " + exception.getMessage());

        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setTimeStamp(new Date());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(request.getServletPath());
        errorDTO.addError(exception.getMessage());

        return errorDTO;
    }


    /// this method is used to catch invalid input fields within HourlyWeatherDto
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);

        ErrorDTO error = new ErrorDTO();
        error.setTimeStamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setPath(((ServletWebRequest) request).getRequest().getServletPath());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {
            error.addError(fieldError.getDefaultMessage());
        });
        return new ResponseEntity<>(error, headers, status);
    }













}




















