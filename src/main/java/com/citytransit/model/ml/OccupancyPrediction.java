package com.citytransit.model.ml;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccupancyPrediction {
    private String vehicleId;
    private double predictedOccupancy;
    private long timestamp;
}
