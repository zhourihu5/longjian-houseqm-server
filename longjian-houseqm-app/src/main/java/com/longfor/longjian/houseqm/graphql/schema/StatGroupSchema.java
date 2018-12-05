package com.longfor.longjian.houseqm.graphql.schema;

import com.longfor.longjian.houseqm.graphql.fetcher.StatGroupDataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 *
 * @author lipeishuai
 * @date 2018/11/29 11:20
 */
@Slf4j
@Service
public class StatGroupSchema {



    @Value("classpath:graphql/stat_group.graphqls")
    private static Resource schemaResource;

    public static GraphQLSchema statGroupSchema = buildSchema();


    /**
     * wiring时只将需要dataFetcher的非普通Field
     *
     * @return
     */
    public static RuntimeWiring buildRuntimeWiring(){
        return RuntimeWiring.newRuntimeWiring().scalar(Scalars.DateField)
                .type("gapiQuery", typeWiring -> typeWiring
                        .dataFetcher("progressStat",StatGroupDataFetcher.progressStatDataFetcher)
                )
                .type("StatGroupItem", typeWiring -> typeWiring
                        .dataFetcher("items",StatGroupDataFetcher.statGroupItemDataFetcher)
               )
                .build();
    }

    /**
     *
     * @return
     */
    public  GraphQLSchema buildSchema1(){

        SchemaGenerator schemaGenerator = new SchemaGenerator();

        InputStreamReader schemaReader = null;
        try {

            InputStream pipelineConfigSchema = getClass().getResourceAsStream("/graphql/pipelineConfig.graphqls");
            schemaReader = new InputStreamReader(schemaResource.getInputStream());
        } catch (IOException e) {
            log.error("buildSchema FileNotFoundException:classpath:graphql/stat_group.graphqls");
        }
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaReader);

        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(
                typeRegistry, wiring);

        return graphQLSchema;
    }

    /**
     *
     * @return
     */
    private static GraphQLSchema buildSchema(){

        log.info("StatGroupSchema#buildSchema ing");

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
