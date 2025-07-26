package com.example.jwttest.common.error;

import com.example.jwttest.common.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

  private final BaseErrorCode errorCode;

  public ApiException(BaseErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public BaseErrorCode getErrorCode() {
    return errorCode;
  }
}
