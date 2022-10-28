package com.simo333.driver.service.impl;

import com.simo333.driver.model.Advice;
import com.simo333.driver.model.Tag;
import com.simo333.driver.payload.advice.AdviceRequest;
import com.simo333.driver.repository.AdviceRepository;
import com.simo333.driver.service.AdviceService;
import com.simo333.driver.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdviceServiceImpl implements AdviceService {

    private final AdviceRepository repository;
    private final TagService tagService;

    @Override
    public Page<Advice> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Page<Advice> findAllByTag(String tagName, Pageable page) {
        log.info("Fetching Advices for tag '{}'. {}.", tagName, page);
        return repository.findAllByTagsName(tagName, page);
    }

    @Override
    public Advice findOne(Long adviceId) {
        return repository.findById(adviceId).orElseThrow(() -> {
            log.error("Answer with id '{}' not found", adviceId);
            return new ResourceNotFoundException(String.format("Answer with id '%s' not found", adviceId));
        });
    }

    @Transactional
    @Override
    public Advice save(AdviceRequest request) {
        log.info("Saving a new Advice '{}'", request.getTitle());
        return repository.save(buildAdvice(request));
    }

    @Transactional
    @Override
    public Advice update(Long adviceId, AdviceRequest request) {
        Advice advice = findOne(adviceId);

        if (request.getTitle() != null) {
            advice.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            advice.setDescription(request.getDescription());
        }
        if (request.getTags() != null) {
            advice.setTags(request.getTags());
        }

        log.info("Updating Advice. After changes: {}", advice);
        return repository.save(advice);
    }

    @Transactional
    @Override
    public void delete(Long adviceId) {
        //TODO check how to delete properly (should delete all related entries)
        repository.deleteById(adviceId);
    }

    private Advice buildAdvice(AdviceRequest request) {
        Set<Tag> tags = new HashSet<>();
        //TODO change after implementing TagDTO
        request.getTags().forEach(tag -> tags.add(tagService.findOne(tag.getId())));
        return Advice.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .tags(tags)
                .build();
    }
}
