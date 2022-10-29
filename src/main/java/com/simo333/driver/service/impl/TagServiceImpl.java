package com.simo333.driver.service.impl;

import com.simo333.driver.exception.TagUniqueViolationException;
import com.simo333.driver.model.Tag;
import com.simo333.driver.payload.tag.TagRequest;
import com.simo333.driver.repository.TagRepository;
import com.simo333.driver.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    @Override
    public Tag findOne(Long tagId) {
        return repository.findById(tagId).orElseThrow(() -> {
            log.error("Tag with id '{}' not found", tagId);
            return new ResourceNotFoundException(String.format("Tag with id '%s' not found", tagId));
        });
    }

    @Override
    public Tag findOne(String tagName) {
        return repository.findByName(tagName).orElseThrow(() -> {
            log.error("Tag with name '{}' not found", tagName);
            return new ResourceNotFoundException(String.format("Tag with name '%s' not found", tagName));
        });
    }

    @Override
    public List<Tag> findAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public Tag save(TagRequest request) {
        if(repository.existsByName(request.getTagName())) {
            log.error("'{}' tag already exists", request.getTagName());
            throw new TagUniqueViolationException(request.getTagName() + " tag already exists.");
        }
        Tag tag = new Tag();
        tag.setName(request.getTagName());
        log.info("Saving a new tag with name: {}", tag.getName());
        return repository.save(tag);
    }

    @Transactional
    @Override
    public Tag update(Long id, TagRequest request) {
        if (repository.existsByName(request.getTagName())) {
            log.error("'{}' tag already exists", request.getTagName());
            throw new TagUniqueViolationException(request.getTagName() + " tag already exists.");
        }
        Tag tag = findOne(id);
        tag.setName(request.getTagName());
        log.info("Updating a new tag: {}", tag);
        return repository.save(tag);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
