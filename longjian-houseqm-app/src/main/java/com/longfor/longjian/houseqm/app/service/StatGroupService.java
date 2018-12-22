package com.longfor.longjian.houseqm.app.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.vo.VariableVo;
import com.longfor.longjian.houseqm.graphql.schema.CachingPreparsedDocumentProvider;
import com.longfor.longjian.houseqm.graphql.schema.StatGroupSchema;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
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

//    @Resource
//    private StatGroupSchema statGroupSchema;

    @Resource
    private CachingPreparsedDocumentProvider cachingPreparsedDocumentProvider;



    /**
     *
     * @param query
     * @param variableVo
     * @return
     */
    public Object execute(String query, VariableVo variableVo ) {

        Map<String, Object> variables = null;
        try {
            variables = PropertyUtils.describe(variableVo);
        } catch (Exception e) {
            log.error("StatGroupService to map: error {}",e);
            throw new LjBaseRuntimeException(410, "to map error");
        }

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
            for(GraphQLError error: errors){
                log.error("StatGroupService graphql error {} details:{}",error.getErrorType(), error.getMessage());
                throw new LjBaseRuntimeException(410, error.getMessage());
            }
        }

        log.debug("StatGroupService#execute - data: {}", data);
        return data;
    }
}
