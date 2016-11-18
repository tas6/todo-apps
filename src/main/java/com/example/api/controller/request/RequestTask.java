package com.example.api.controller.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestTask {

  @NotNull
  @Length(min = 1, max = 100)
  String subject;

  @NotNull
  @Length(min = 1, max = 500)
  String description;

  Boolean done;

  Integer version;

}
