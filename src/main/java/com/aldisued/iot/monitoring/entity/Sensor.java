package com.aldisued.iot.monitoring.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table(name = "sensors",
uniqueConstraints = @UniqueConstraint(
    name = "uk_sensor_name",
    columnNames = "name"
))
@Entity
public class Sensor {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SensorType type;

  @OneToMany(mappedBy = "sensor")
  private List<SensorReading> sensorReadings = new ArrayList<>();

  @OneToMany(mappedBy = "sensor")
  private List<Alert> alerts = new ArrayList<>();

  public Sensor() {}

  public Sensor(String name, SensorType type) {
    this.name = name;
    this.type = type;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SensorType getType() {
    return type;
  }

  public void setType(SensorType type) {
    this.type = type;
  }

  public List<Alert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<Alert> alerts) {
    this.alerts = alerts;
  }

  public List<SensorReading> getSensorReadings() {
    return sensorReadings;
  }

  public void setSensorReadings(
      List<SensorReading> sensorReadings) {
    this.sensorReadings = sensorReadings;
  }

  //setting both sides here for self-containment, sensor reading side is unneccessary because of its constructor overload but harmless
  public void addSensorReading(SensorReading sensorReading) {
    Objects.requireNonNull(sensorReading, "Reading can't be null");
    sensorReading.setSensor(this);
    sensorReadings.add(sensorReading);
  }

  public void addAlert(Alert alert) {
    Objects.requireNonNull(alert, "Alert can't be null");
    alert.setSensor(this);
    alerts.add(alert);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sensor sensor = (Sensor) o;
    return Objects.equals(id, sensor.id) && Objects.equals(name, sensor.name)
        && type == sensor.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, type);
  }
}
