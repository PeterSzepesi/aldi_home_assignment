package com.aldisued.iot.monitoring.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException{

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
