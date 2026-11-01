package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.exception.ResourceNotFoundException;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AlertService {

  private final AlertRepository alertRepository;
  private final SensorRepository sensorRepository;
  private final KafkaTemplate<String, AlertDto> kafkaTemplate;
  private static final Logger LOGGER = LoggerFactory.getLogger(AlertService.class);

  public AlertService(AlertRepository alertRepository, SensorRepository sensorRepository,
      KafkaTemplate<String, AlertDto> kafkaTemplate) {
    this.alertRepository = alertRepository;
    this.sensorRepository = sensorRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Transactional
  public Alert saveAlert(@NotNull @Valid AlertDto alertDto) {
    Sensor sensor = sensorRepository.findById(alertDto.sensorId())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Sensor not found for ID: " + alertDto.sensorId()
        ));

    Alert alert = new Alert(
        alertDto.message(),
        alertDto.timestamp(),
        sensor
    );

    sensor.addAlert(alert);

    Alert savedAlert = alertRepository.saveAndFlush(alert);

    /* Few pitfalls here:
    * DB commit failure can create published event without saved alert -> saveFlush throws write failures before publishing
    * Publishing error on the other hand can create saved alert without corresponding publised event, since its done asynchronously -> logging publishing failures inside the CompletableFuture
    * */

    kafkaTemplate.send("alerts", alertDto)
        .whenComplete((result,failure) -> {
          if(failure != null) {
            LOGGER.error(
                "Kafka publication failed for alertId={} sensorId={}",
                savedAlert.getId(),
                alertDto.sensorId(),
                failure);
          }
        });

    return savedAlert;
  }

  @Transactional(readOnly = true)
  public AlertDto findLastAlertBySensorId(UUID sensorId) {
    Alert alert = alertRepository.
        findFirstBySensor_IdOrderByTimestampDescIdDesc(sensorId)
        .orElseThrow(() -> new ResourceNotFoundException("No alerts were found for sensor with ID: " + sensorId));

    return new AlertDto(
        sensorId,
        alert.getMessage(),
        alert.getTimestamp()
    );
  }
}
