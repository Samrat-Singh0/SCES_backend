package com.f1soft.sces.util;

import com.f1soft.sces.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ResponseBuilder {

  public static <T> ResponseEntity<ResponseDto> success(String message, T body) {
    return new ResponseEntity<>(new ResponseDto(true, message, body), HttpStatus.OK);
  }

  public static ResponseEntity<ResponseDto> error(HttpStatus status, String message) {
    return new ResponseEntity<>(new ResponseDto(false, message, null), status);
  }

  public static ResponseEntity<ResponseDto> getFailedMessage(String message) {
    return new ResponseEntity<>(new ResponseDto(false, message, null), HttpStatus.OK);
  }

}
