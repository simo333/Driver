package com.simo333.driver.controller;

import com.simo333.driver.model.Advice;
import com.simo333.driver.payload.advice.AdviceRequest;
import com.simo333.driver.service.AdviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/advices")
@RequiredArgsConstructor
public class AdviceController {

    private final AdviceService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Advice> getAllAdvices(@RequestParam(required = false) String tag, Pageable page) {
        if(tag == null) {
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
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
