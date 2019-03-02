package com.longfor.longjian.houseqm.app.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.vo.VariableVo;
import com.longfor.longjian.houseqm.graphql.schema.CachingPreparsedDocumentProvider;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.schema.GraphQLSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Graphql的执行服务，每个微服务可以使用一个这个服务即可
 *
 * @author lipeishuai
 * @date 2018/11/29 16:04
 */

@Slf4j
@Service
public class GraphqlExecuteService {

    @Resource
    private CachingPreparsedDocumentProvider cachingPreparsedDocumentProvider;


    public Object execute(Integer groupId, String serviceName, String query, VariableVo variableVo, GraphQLSchema graphQLSchema) {

        Map<String, Object> variables = null;
        try {
            variables = PropertyUtils.describe(variableVo);
        } catch (Exception e) {
            log.error("{} to map: error {}", serviceName, e);
            throw new LjBaseRuntimeException(410, "to map error");
        }

        log.debug("{}#execute - variableVo categoryKey :{}", serviceName, variableVo.getCategoryKey());


        Cache<String, PreparsedDocumentEntry> cache = cachingPreparsedDocumentProvider.getCache();
        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema)
                .preparsedDocumentProvider(cache::get)
                .build();

        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(query).context(groupId).variables(variables).build();
        ExecutionResult executionResult = graphQL.execute(executionInput);

        Object data = executionResult.getData();
        List<GraphQLError> errors = executionResult.getErrors();


        if (CollectionUtils.isNotEmpty(errors)) {
            log.error("{}#graphql error {} details:{}", serviceName, errors.get(0).getErrorType(), errors.get(0).getMessage());
            throw new LjBaseRuntimeException(410, errors.get(0).getMessage());
        }

        log.debug("{}#execute - data: {}", serviceName, data);
        return data;
    }

}
