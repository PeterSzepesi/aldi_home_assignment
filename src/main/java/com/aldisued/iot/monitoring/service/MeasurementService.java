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

  @Transactional(readOnly = true)
  public List<Double> getMeasurementValuesBySensorType(SensorType sensorType, LocalDateTime from,
      LocalDateTime to) {

    if(sensorType == null) {
      throw new IllegalArgumentException("sensorType must not be null");
    }

    if(from == null || to == null) {
      throw new IllegalArgumentException("from and to must not be null");
    }

    if(from.isAfter(to)) {
      throw new IllegalArgumentException("from must be before or equal to to");
    }

    return sensorReadingRepository.findMeasurementValuesBySensorType(
        sensorType,
        from,
        to
    );
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

  //Sidenotes: Both of these could be solved by Java streams, but for millions/tens of millions of records, loading the complete entities from DB would be costly.
  // DB indexing could further improve sensor reading lookup
  // for task 8 since there could be millions of readings per type pagination might also be useful. Fractional seconds in case of sensors might also be an edge case

}
