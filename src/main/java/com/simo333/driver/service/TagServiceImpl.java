package com.simo333.driver.service;

import com.simo333.driver.model.Tag;
import com.simo333.driver.repository.TagRepository;
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
    public Tag save(Tag tag) {
        return repository.save(tag);
    }

    @Transactional
    @Override
    public Tag update(Tag tag) {
        findOne(tag.getId());
        return repository.save(tag);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
