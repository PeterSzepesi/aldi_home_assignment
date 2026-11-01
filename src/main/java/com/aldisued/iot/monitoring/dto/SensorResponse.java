package com.aldisued.iot.monitoring.dto;

import com.aldisued.iot.monitoring.entity.SensorType;

import java.util.UUID;

public record SensorResponse(
    UUID id,
    String name,
    SensorType type
) {}
