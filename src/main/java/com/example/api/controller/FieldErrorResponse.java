package com.example.api.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldErrorResponse {

  private String message;
  private String fieldName;
  private String objectName;

  public FieldErrorResponse(String message) {
    this.message = message;
  }

}