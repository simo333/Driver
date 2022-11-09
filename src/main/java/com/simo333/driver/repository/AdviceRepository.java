package com.simo333.driver.repository;

import com.simo333.driver.model.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdviceRepository extends JpaRepository<Advice, Long> {

    Page<Advice> findAllByTagsName(String tagName, Pageable page);

    boolean existsByTitle(String title);
}
