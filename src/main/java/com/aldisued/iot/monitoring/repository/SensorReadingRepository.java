package com.aldisued.iot.monitoring.repository;

import com.aldisued.iot.monitoring.entity.SensorReading;
import com.aldisued.iot.monitoring.entity.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

  @Query("""
        select avg(reading.value)
        from SensorReading reading
        where reading.sensor.type = :sensorType
        and reading.timestamp >= :from
        and reading.timestamp <= :to
        """)
  Optional<Double> findAverageValue(
      @Param("sensorType")SensorType sensorType,
      @Param("from") LocalDateTime from,
      @Param("to")LocalDateTime to
      );

  @Query("""
        select reading.value
        from SensorReading reading
        where reading.sensor.type = :sensorType
        and reading.timestamp >= :from
        and reading.timestamp <= :to
        order by reading.timestamp asc, reading.id asc
        """)
  List<Double> findMeasurementValuesBySensorType(
      @Param("sensorType")SensorType sensorType,
      @Param("from") LocalDateTime from,
      @Param("to")LocalDateTime to
  );
}
