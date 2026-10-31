package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.entity.SensorType;
import com.aldisued.iot.monitoring.repository.SensorReadingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeasurementService {

  private final SensorReadingRepository sensorReadingRepository;

  public MeasurementService(SensorReadingRepository sensorReadingRepository) {
    this.sensorReadingRepository = sensorReadingRepository;
  }

  public List<Double> getMeasurementValuesBySensorType(SensorType sensorType, LocalDateTime from,
      LocalDateTime to) {
    // TODO: Task 8
    return List.of();
  }

  @Transactional(readOnly = true)
  public Optional<Double> getAverageTemperature(LocalDateTime from, LocalDateTime to) {
    if(from == null || to == null) {
      throw new IllegalArgumentException("from and to must not be null");
    }

    if(from.isAfter(to)) {
      throw new IllegalArgumentException("from must be before or equal to to");
    }

    return sensorReadingRepository.findAverageValue(
        SensorType.TEMPERATURE,
        from,
        to
    );
  }

}
