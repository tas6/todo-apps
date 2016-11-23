package com.example.api.controller;

import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.api.exception.InvalidVersionException;
import com.example.api.exception.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    if (body instanceof ErrorResponse == false) {
      body = new ErrorResponse(ex.getMessage());
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ErrorResponse errorResponse = new ErrorResponse("Invalid Arguments");
    errorResponse.fieldErrors =
        ex.getBindingResult().getFieldErrors()
            .stream()
            .map(e -> createErrorDetail(e, request.getLocale()))
            .collect(Collectors.toList());

    return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
  }

  @ExceptionHandler({ ResourceNotFoundException.class })
  public ResponseEntity<Object> handleException(ResourceNotFoundException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse("Resource Not Found");
    return super.handleExceptionInternal(ex, errorResponse, null, HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler({ InvalidVersionException.class })
  public ResponseEntity<Object> handleException(InvalidVersionException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse("Optimistic Lock Failed");
    return super.handleExceptionInternal(ex, errorResponse, null, HttpStatus.CONFLICT, request);
  }

  public FieldErrorResponse createErrorDetail(FieldError fieldError, Locale locale) {
    return new FieldErrorResponse(
        messageSource.getMessage(fieldError, locale),
        fieldError.getField(),
        fieldError.getObjectName());
  }

}
