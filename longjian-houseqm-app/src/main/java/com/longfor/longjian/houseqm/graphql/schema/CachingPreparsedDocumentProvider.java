package com.longfor.longjian.houseqm.graphql.schema;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author lipeishuai
 * @date 2018/11/30 14:24
 */

@Service
@Slf4j
@Data
public class CachingPreparsedDocumentProvider {

    private Cache<String, PreparsedDocumentEntry> cache;

    @PostConstruct
    public void setUp() {
        cache = Caffeine.newBuilder().maximumSize(10_000).build();
    }

}