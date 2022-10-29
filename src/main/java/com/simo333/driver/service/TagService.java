package com.simo333.driver.service;

import com.simo333.driver.model.Tag;
import com.simo333.driver.payload.tag.TagRequest;

import java.util.List;

public interface TagService {

    Tag findOne(Long tagId);

    Tag findOne(String tagName);

    List<Tag> findAll();

    Tag save(TagRequest request);

    Tag update(Long tagId, TagRequest request);

    void delete(Long tagId);

}