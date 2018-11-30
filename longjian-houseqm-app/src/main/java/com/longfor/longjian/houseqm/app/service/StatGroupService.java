package com.longfor.longjian.houseqm.app.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.req.StatGroupReq;
import com.longfor.longjian.houseqm.graphql.schema.CachingPreparsedDocumentProvider;
import com.longfor.longjian.houseqm.graphql.schema.StatGroupSchema;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lipeishuai
 * @date 2018/11/29 16:04
 */

@Slf4j
@Service
public class StatGroupService {

    @Resource
    private StatGroupSchema statGroupSchema;

    @Resource
    private CachingPreparsedDocumentProvider cachingPreparsedDocumentProvider;



    /**
     *
     * @param query
     * @param variableVo
     * @return
     */
    public Object execute(String query, StatGroupReq.VariableVo variableVo ){

        Map<String, Object> variables = variableVo.toMap();

        log.debug("StatGroupService#execute - variableVo categoryKey :{}", variableVo.getCategoryKey());


        Cache<String, PreparsedDocumentEntry> cache = cachingPreparsedDocumentProvider.getCache();
        GraphQL graphQL = GraphQL.newGraphQL(StatGroupSchema.statGroupSchema)
                .preparsedDocumentProvider(cache::get)
                .build();

        ExecutionInput executionInput =  ExecutionInput.newExecutionInput().query(query).variables(variables).build();
        ExecutionResult executionResult = graphQL.execute(executionInput);

        Object data = executionResult.getData();
        List<GraphQLError> errors = executionResult.getErrors();

        if(CollectionUtils.isNotEmpty(errors)){
            throw new LjBaseRuntimeException(401,"StatGroupService graphql 执行出错");
        }

        log.debug("StatGroupService#execute - data: {}", data);
        return data;
    }
}
