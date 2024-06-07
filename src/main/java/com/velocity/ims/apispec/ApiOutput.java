package com.velocity.ims.apispec;

import lombok.Data;

@Data
public class ApiOutput<T> {

  protected boolean success = true;
  protected ApiError error;
  protected T response;

  public ApiOutput() {
  }

  public ApiOutput(T response) {
    this.response = response;
  }

  public ApiOutput(ApiError apiError) {
    this.success = false;
    error = apiError;
  }

}
