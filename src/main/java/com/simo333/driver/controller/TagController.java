package com.simo333.driver.controller;

import com.simo333.driver.model.Tag;
import com.simo333.driver.payload.tag.TagRequest;
import com.simo333.driver.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService service;

    @GetMapping
    @ResponseStatus(OK)
    public List<Tag> getAllTags() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Tag getById(@PathVariable Long id) {
        return service.findOne(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(OK)
    public Tag getByName(@PathVariable String name) {
        return service.findOne(name);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Tag save(@RequestBody @Valid TagRequest request) {
        return service.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public Tag update(@RequestBody @Valid TagRequest request, @PathVariable Long id) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
