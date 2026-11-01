package com.aldisued.iot.monitoring.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record SensorReadingDto(
    @NotNull(message = "Sensor ID is required")
    UUID sensorId,
    @NotNull(message = "Reading value is required")
    Double value,
    @NotNull(message = "Reading timestamp is required")
    LocalDateTime timestamp
) {}
