package com.simo333.driver.service;

import com.simo333.driver.model.TrainingEntry;
import com.simo333.driver.payload.training.TrainingResultRequest;
import com.simo333.driver.payload.training.TrainingResultResponse;

public interface TrainingEntryService {

    TrainingEntry conductNewTraining(Long adviceId);

    TrainingResultResponse verifyTraining(TrainingResultRequest request);
}
