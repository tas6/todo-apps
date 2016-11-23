package com.example.api.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  String error;
  String message;
  List<FieldErrorResponse> fieldErrors;

  public ErrorResponse(String message) {
    this.message     = message;
    this.fieldErrors = new ArrayList<>();
  }

}