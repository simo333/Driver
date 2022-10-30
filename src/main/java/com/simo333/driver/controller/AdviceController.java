package com.simo333.driver.controller;

import com.simo333.driver.model.Advice;
import com.simo333.driver.model.TrainingEntry;
import com.simo333.driver.payload.advice.AdviceCreateRequest;
import com.simo333.driver.payload.advice.AdviceQuestionsRequest;
import com.simo333.driver.payload.advice.AdviceUpdateRequest;
import com.simo333.driver.payload.training.TrainingResultRequest;
import com.simo333.driver.payload.training.TrainingResultResponse;
import com.simo333.driver.service.AdviceService;
import com.simo333.driver.service.TrainingEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
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

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Advice getById(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Advice save(@RequestBody @Valid AdviceCreateRequest request) {
        return service.save(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public Advice update(@PathVariable Long id, @RequestBody @Valid AdviceUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/add-questions")
    @ResponseStatus(OK)
    public void addQuestionsToAdvice(@PathVariable Long id, @RequestBody @Valid AdviceQuestionsRequest request) {
        service.addQuestionsToAdvice(id, request);
    }

    @DeleteMapping("/{id}/remove-questions")
    @ResponseStatus(OK)
    public void removeQuestionsFromAdvice(@PathVariable Long id, @RequestBody @Valid AdviceQuestionsRequest request) {
        service.removeQuestionsFromAdvice(id, request);
    }

    @GetMapping("/{id}/training")
    @ResponseStatus(OK)
    public TrainingEntry sendTrainingQuestions(@PathVariable("id") Long adviceId) {
        return trainingService.conductNewTraining(adviceId);
    }

    @PostMapping("/verify-training")
    @ResponseStatus(OK)
    public TrainingResultResponse verifyTraining(@RequestBody @Valid TrainingResultRequest result) {
        return trainingService.verifyTraining(result);
    }
}
