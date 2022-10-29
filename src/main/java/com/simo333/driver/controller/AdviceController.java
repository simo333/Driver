package com.simo333.driver.controller;

import com.simo333.driver.model.Advice;
import com.simo333.driver.model.TrainingEntry;
import com.simo333.driver.payload.advice.AdviceRequest;
import com.simo333.driver.payload.training.TrainingResultRequest;
import com.simo333.driver.payload.training.TrainingResultResponse;
import com.simo333.driver.service.AdviceService;
import com.simo333.driver.service.TrainingEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/advices")
@RequiredArgsConstructor
public class AdviceController {

    private final AdviceService service;
    private final TrainingEntryService trainingService;

    @GetMapping
    @ResponseStatus(OK)
    public Page<Advice> getAllAdvices(@RequestParam(required = false) String tag, Pageable page) {
        if (tag == null) {
            return service.findAll(page);
        }
        return service.findAllByTag(tag, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Advice save(@RequestBody @Valid AdviceRequest request) {
        return service.save(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Advice update(@PathVariable Long id, @RequestBody @Valid AdviceRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/training")
    @ResponseStatus(OK)
    public TrainingEntry sendTrainingQuestions(@PathVariable("id") Long adviceId) {
        return trainingService.conductNewTraining(adviceId);
    }

    @PostMapping("/verify-training/{id}")
    @ResponseStatus(OK)
    public TrainingResultResponse verifyTraining(@RequestBody TrainingResultRequest result, @PathVariable Long id) {
        return trainingService.verifyTraining(result);
    }
}
