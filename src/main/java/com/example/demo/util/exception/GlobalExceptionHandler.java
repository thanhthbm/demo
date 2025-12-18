package com.example.demo.util.exception;

import com.example.demo.domain.dto.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = {
      IdInvalidException.class,
      ResourceNotFoundException.class,
      ResourceAlreadyExistException.class
  })
  public ResponseEntity<RestResponse<Object>> handleCustomException(Exception ex) {
    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(ex.getMessage());
    res.setData(null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }
}
