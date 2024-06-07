package com.velocity.ims.exception;


import org.springframework.http.HttpStatus;

public class ApiErrorException extends RuntimeException {

  private final HttpStatus status;

  public ApiErrorException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getHttpStatus() {
    return this.status;
  }


}
