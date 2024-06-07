package com.velocity.ims.exception;


import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.velocity.ims.apispec.ApiError;
import com.velocity.ims.apispec.ApiOutput;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApiErrorException.class)
  protected ResponseEntity<Object> handleApiErrorException(ApiErrorException e, HttpServletResponse response) {
    ApiOutput<ApiError> output = new ApiOutput<>(new ApiError(e.getHttpStatus(), e.getMessage(), e));
    return buildResponseEntity(output, e.getHttpStatus());
  }


  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                    WebRequest request) {
    ApiError apiError = new ApiError(BAD_REQUEST);
    apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
        ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
    apiError.setDebugMessage(ex.getMessage());
    ApiOutput<ApiError> output = new ApiOutput<>(apiError);
    return buildResponseEntity(output, BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    StringBuilder errorMessage = new StringBuilder();
    List<String> validationList = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> fieldError.getDefaultMessage())
        .collect(Collectors.toList());
    ApiError apiError = new ApiError(BAD_REQUEST);
    for (String s : validationList) {
      errorMessage.append(s).append("\n ");
    }
    apiError.setDebugMessage(errorMessage.toString());
    apiError.setMessage(errorMessage.toString());
    return new ResponseEntity<>(apiError, status);
  }

  private <T> ResponseEntity<Object> buildResponseEntity(ApiOutput<T> apiOutput, HttpStatus status) {
    return new ResponseEntity<>(apiOutput, status);
  }


}
