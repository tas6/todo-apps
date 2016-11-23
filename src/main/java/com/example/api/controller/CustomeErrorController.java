package com.example.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class CustomeErrorController implements ErrorController {

  private static final String PATH = "/error";

  @Autowired
  ErrorAttributes errorAttributes;

  @Value("${debug}")
  boolean debug;

  @RequestMapping(value = PATH)
  public ErrorResponse error(HttpServletRequest request) {
    Map<String, Object> errorAttributes = getErrorAttributes(request, debug);
    return new ErrorResponse(errorAttributes.get("message").toString());
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }

  private Map<String, Object> getErrorAttributes(
      HttpServletRequest request,
      boolean includeStackTrace) {

    return errorAttributes.getErrorAttributes(
        new ServletRequestAttributes(request),
        includeStackTrace);

  }

}