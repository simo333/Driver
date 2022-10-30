package com.simo333.driver.service.impl;

import com.simo333.driver.exception.UniqueViolationException;
import com.simo333.driver.model.Advice;
import com.simo333.driver.model.Question;
import com.simo333.driver.model.Tag;
import com.simo333.driver.payload.advice.AdviceCreateRequest;
import com.simo333.driver.payload.advice.AdviceQuestionsRequest;
import com.simo333.driver.payload.advice.AdviceUpdateRequest;
import com.simo333.driver.repository.AdviceRepository;
import com.simo333.driver.service.AdviceService;
import com.simo333.driver.service.QuestionService;
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
    private final QuestionService questionService;

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
            log.error("Advice with id '{}' not found", adviceId);
            return new ResourceNotFoundException(String.format("Advice with id '%s' not found", adviceId));
        });
    }

    @Transactional
    @Override
    public Advice save(AdviceCreateRequest request) {
        checkTitleUnique(request.getTitle());
        log.info("Saving a new Advice '{}'", request.getTitle());
        return repository.save(buildAdvice(request));
    }

    @Transactional
    @Override
    public Advice update(Long adviceId, AdviceUpdateRequest request) {
        Advice advice = findOne(adviceId);

        if (request.getTitle() != null) {
            checkTitleUnique(request.getTitle());
            advice.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            advice.setDescription(request.getDescription());
        }
        AdviceUpdateRequest.Option option = request.getTagsChange();
        if (option == AdviceUpdateRequest.Option.REMOVE_ALL) {
            advice.setTags(new HashSet<>());
        } else if (option == AdviceUpdateRequest.Option.REPLACE) {
            Set<Tag> tags = new HashSet<>();
            if (request.getTags() != null) {
                request.getTags().forEach(tagName -> tags.add(tagService.findOne(tagName)));
            }
            advice.setTags(tags);
        }

        log.info("Updating Advice. After changes: {}", advice);
        return repository.save(advice);
    }

    @Transactional
    @Override
    public void delete(Long adviceId) {
        repository.deleteById(adviceId);
    }

    @Transactional
    @Override
    public void addQuestionsToAdvice(Long adviceId, AdviceQuestionsRequest request) {
        Advice advice = findOne(adviceId);
        Set<Question> questions = new HashSet<>();
        request.getQuestions().forEach(id -> questions.add(questionService.findOne(id)));
        advice.getQuestions().addAll(questions);
    }

    @Transactional
    @Override
    public void removeQuestionsFromAdvice(Long adviceId, AdviceQuestionsRequest request) {
        Advice advice = findOne(adviceId);
        Set<Question> questions = new HashSet<>();
        request.getQuestions().forEach(id -> questions.add(questionService.findOne(id)));
        advice.getQuestions().removeAll(questions);

    }

    private Advice buildAdvice(AdviceCreateRequest request) {
        Set<Tag> tags = new HashSet<>();
        if (request.getTagNames() != null) {
            request.getTagNames().forEach(tagName -> tags.add(tagService.findOne(tagName)));
        }
        return Advice.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .tags(tags)
                .build();
    }

    private void checkTitleUnique(String title) {
        if (repository.existsByTitle(title)) {
            log.error("Advice with title '{}' already exists", title);
            throw new UniqueViolationException(title + " title already exists.");
        }
    }
}
