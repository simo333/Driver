package com.simo333.driver.controller;

import com.simo333.driver.model.Tag;
import com.simo333.driver.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAllTags() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Tag getById(@PathVariable Long id) {
        return service.findOne(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Tag getByName(@PathVariable String name) {
        return service.findOne(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag save(@RequestBody @Valid Tag tag) {
        return service.save(tag);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Tag update(@RequestBody @Valid Tag tag, @PathVariable Long id) {
        tag.setId(id);
        return service.update(tag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
