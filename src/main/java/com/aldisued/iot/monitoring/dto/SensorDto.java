package com.aldisued.iot.monitoring.dto;

import com.aldisued.iot.monitoring.entity.SensorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SensorDto(
    @NotBlank(message = "Sensor name is required")
    String name,
    @NotNull(message = "Sensor type is required")
    SensorType type
) {
}
