package com.simo333.driver.service;

import com.simo333.driver.model.Tag;

import java.util.List;

public interface TagService {

    Tag findOne(Long tagId);

    Tag findOne(String tagName);

    List<Tag> findAll();

    boolean existsByName(String tagName);

    Tag save(Tag tag);

    Tag update(Tag tag);

    void delete(Long id);

}
