package com.aldisued.iot.monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlertDto(
    @NotNull(message = "Alert ID is required")
    UUID sensorId,
    @NotBlank(message = "Alert message is required")
    @Size(max = 255, message = "Alert message can't contain more than 255 characters")
    String message,
    @NotNull(message = "Alert timestamp is required")
    LocalDateTime timestamp
) {
}
