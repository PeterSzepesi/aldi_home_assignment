package com.aldisued.iot.monitoring.controller;


import com.aldisued.iot.monitoring.exception.ResourceNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleDbConstraint(ConstraintViolationException exception) {

    LOGGER.error(
        "Unexpected database constraint failure: constraint = {}",
        exception.getConstraintName(),
        exception
    );

    if("uk_sensor_name".equals(exception.getConstraintName())) {
      return ProblemDetail.forStatusAndDetail(
          HttpStatus.CONFLICT,
          "A sensor with the name provided already exists"
      );
    }

    throw exception;
  }

  @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
  public ProblemDetail handleInvalidInput(jakarta.validation.ConstraintViolationException exception) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST,
        "Request contains invalid or missing values");

    problem.setProperty("errors", exception.getConstraintViolations().stream()
        .map(violation -> violation.getMessage())
        .distinct()
        .sorted()
        .toList()
    );

    return problem;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFound(ResourceNotFoundException exception) {
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND,
        exception.getMessage()
    );
  }
}
