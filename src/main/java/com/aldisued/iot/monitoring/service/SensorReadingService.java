package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.exception.ResourceNotFoundException;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class SensorReadingService {

  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;

  public SensorReadingService(SensorReadingRepository sensorReadingRepository,
      SensorRepository sensorRepository) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
  }

  @Transactional
  public SensorReading saveSensorReading(@NotNull @Valid SensorReadingDto sensorReadingDto) {
    Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId())
        .orElseThrow(() -> new ResourceNotFoundException("Sensor not found for ID: " + sensorReadingDto.sensorId()));

    SensorReading sensorReading = new SensorReading(
        sensorReadingDto.value(),
        sensorReadingDto.timestamp(),
        sensor
    );

    sensor.addSensorReading(sensorReading);

    return sensorReadingRepository.save(sensorReading);
  }
}
