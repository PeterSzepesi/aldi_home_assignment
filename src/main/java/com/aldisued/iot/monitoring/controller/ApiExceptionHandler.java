package com.aldisued.iot.monitoring.controller;


import com.aldisued.iot.monitoring.exception.ResourceNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleDbConstraint(ConstraintViolationException exception) {

    if("uk_sensor_name".equals(exception.getConstraintName())) {
      return ProblemDetail.forStatusAndDetail(
          HttpStatus.CONFLICT,
          "A sensor with the name provided already exists"
      );
    }

    throw exception;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFound(ResourceNotFoundException exception) {
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND,
        exception.getMessage()
    );
  }
}
