package com.aldisued.iot.monitoring.controller;

import com.aldisued.iot.monitoring.dto.SensorDto;
import com.aldisued.iot.monitoring.dto.SensorResponse;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensors")
public class SensorController {

  private final SensorService sensorService;

  public SensorController(SensorService sensorService) {
    this.sensorService = sensorService;
  }

  @PostMapping
  public SensorResponse saveSensor(@Valid @RequestBody SensorDto sensorDto) {
    Sensor saved = sensorService.saveSensor(sensorDto);

    return new SensorResponse(
        saved.getId(),
        saved.getName(),
        saved.getType()
    );
  }
}