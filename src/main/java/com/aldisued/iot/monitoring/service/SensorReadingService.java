package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.SensorReadingDto;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SensorReadingService {

  private final SensorReadingRepository sensorReadingRepository;
  private final SensorRepository sensorRepository;

  public SensorReadingService(SensorReadingRepository sensorReadingRepository,
      SensorRepository sensorRepository) {
    this.sensorReadingRepository = sensorReadingRepository;
    this.sensorRepository = sensorRepository;
  }

  @Transactional
  public SensorReading saveSensorReading(SensorReadingDto sensorReadingDto) {
    Sensor sensor = sensorRepository.findById(sensorReadingDto.sensorId())
        .orElseThrow(() -> new EntityNotFoundException("Sensor not found with ID: " + sensorReadingDto.sensorId()));

    SensorReading sensorReading = new SensorReading(
        sensorReadingDto.value(),
        sensorReadingDto.timestamp(),
        sensor
    );

    sensor.getSensorReadings().add(sensorReading);

    return sensorReadingRepository.save(sensorReading);
  }

}
