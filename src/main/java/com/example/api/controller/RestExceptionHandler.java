package com.example.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrors(
        ex.getBindingResult().getFieldErrors()
            .stream()
            .map(e -> new ErrorDetail(e, request))
            .collect(Collectors.toList()));

    return super.handleExceptionInternal(ex, errorResponse, headers, status, request);
  }

  @Data
  public class ErrorResponse {
    List<ErrorDetail> errors;
  }

  @Data
  @AllArgsConstructor
  public class ErrorDetail {
    private String code;
    private String fieldName;
    private String objectName;
    private String message;

    public ErrorDetail(FieldError fieldError, WebRequest request) {
      this.code       = fieldError.getCode();
      this.fieldName  = fieldError.getField();
      this.objectName = fieldError.getObjectName();
      this.message    = messageSource.getMessage(fieldError, request.getLocale());
    }
  }
}
