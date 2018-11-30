package com.longfor.longjian.houseqm.graphql.schema;

import com.longfor.longjian.houseqm.graphql.fetcher.StatGroupDataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author lipeishuai
 * @date 2018/11/29 11:20
 */
@Slf4j
@Service
public class StatGroupSchema {

    @Resource
    StatGroupDataFetcher statGroupDataFetcher;


    /**
     * wiring时只将需要dataFetcher的非普通Field
     *
     * @return
     */
    public RuntimeWiring buildRuntimeWiring(){
        return RuntimeWiring.newRuntimeWiring()
                .type("gapiQuery", typeWiring -> typeWiring
                        .dataFetcher("progressStat",statGroupDataFetcher.progressStatDataFetcher)
                )
                .type("StatGroupItem", typeWiring -> typeWiring
                        .dataFetcher("items",statGroupDataFetcher.statGroupItemDataFetcher)
               )
                .build();
    }

    /**
     *
     * @return
     */
    public GraphQLSchema buildSchema(){

        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        File schemaFile=null;
        try {
            schemaFile = ResourceUtils.getFile("classpath:graphql/stat_group.graphqls");

        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException:classpath:graphql/stat_group.graphqls");
        }


        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);

        return graphQLSchema;
    }

}
