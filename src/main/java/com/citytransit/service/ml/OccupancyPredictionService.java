package com.citytransit.service.ml;

import com.citytransit.model.ml.OccupancyPrediction;
import org.springframework.stereotype.Service;

@Service
public class OccupancyPredictionService {

    public OccupancyPrediction predict(String vehicleId) {
        // Lógica de predicción de ocupación (placeholder)
        return new OccupancyPrediction(vehicleId, Math.random(), System.currentTimeMillis());
    }
}
