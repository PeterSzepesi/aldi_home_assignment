package com.aldisued.iot.monitoring.service;


import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
    if(deviation == null || deviation < 0.0
        || deviation > 1.0 || !Double.isFinite(deviation)) {
      throw new IllegalArgumentException("Deviation must be a finite number between 0.0 and 1.0");
    }

    if(values == null) {
      throw new IllegalArgumentException("Values can't be null");
    }

    if(values.isEmpty()) {
      return List.of();
    }

    double average = values.stream()
        .mapToDouble(value -> {
          if(value == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Measurements can only contain finite numbers");
          }
          return value;
        })
        .average()
        .orElseThrow();

    double acceptableDeviation = Math.abs(average) * deviation;

    return values.stream()
        .filter(value -> Math.abs(value - average) <= acceptableDeviation)
        .toList();
  }

  public List<Double> getMovingAverage(List<Double> data, int windowSize) {
    // TODO: Task 10
    return List.of();
  }

}
