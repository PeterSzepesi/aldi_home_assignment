package com.aldisued.iot.monitoring.service;


import java.util.ArrayList;
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
    //rolling window, rolling sum
    if(data == null) {
      throw new IllegalArgumentException("Data cant be null");
    }

    if(windowSize <= 0 || windowSize > data.size()) {
      throw new IllegalArgumentException("Window size must be between 1 and the number of data elements");
    }

    double[] values = data.stream()
        .mapToDouble(v -> {
          if(v == null || !Double.isFinite(v)) {
            throw new IllegalArgumentException("Data measurements must be finite numbers");
          }
          return v;
        })
        .toArray();

    if(windowSize == 1) {
      return  List.copyOf(data);
    }

    List<Double> averages = new ArrayList<>();
    double windowSum = 0.0;

    for(int i = 0; i < values.length; i++) {
      if(i >= windowSize) {
        windowSum -= values[i - windowSize];
      }

      windowSum += values[i];

      if(i >= windowSize - 1){
        averages.add(windowSum/windowSize);
      }
    }

    return List.copyOf(averages);
  }
}
