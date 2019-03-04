package com.longfor.longjian.houseqm.graphql.schema;

import com.longfor.longjian.houseqm.graphql.data.DateScalarType;
import com.longfor.longjian.houseqm.graphql.fetcher.GroupProgressStatDataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;

/**
 * typeWiring -> typeWiring.dataFetcher
 * JDK1.8 特殊语法： -> 前面是参数，后面是返回
 *
 * @author lipeishuai
 * @date 2018/11/29 11:20
 */
@Slf4j
@Service
public class GroupProgressStatSchema {


    public static final String JAR_TYPE = "jar";

    public static final GraphQLScalarType DateField = new DateScalarType();

    @Value("classpath:graphql/progressStat.graphqls")
    private static Resource schemaResource;

    @javax.annotation.Resource
    GroupProgressStatDataFetcher statGroupDataFetcher;

    /**
     * wiring时只将需要dataFetcher的非普通Field
     *
     * @return
     */
    public RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring().scalar(DateField)
                .type("gapiQuery", typeWiring -> typeWiring
                        .dataFetcher("progressStat", statGroupDataFetcher.progressStatDataFetcher)
                )
                .type("StatGroupItem", typeWiring -> typeWiring
                        .dataFetcher("items", statGroupDataFetcher.statGroupItemDataFetcher)
                ).type("timeFrameType", typeWiring -> typeWiring
                        .enumValues(GroupProgressStatDataFetcher.timeFrameTypeResolver)
                )
                .build();
    }

    /**
     * 1. 解析Schema文件
     * 2. buildRuntimeWiring：Schema中定义的类型-获取数据
     * <p>
     * Cleanup：注解在输入输出流等需要释放资源的变量上，不需要写额外繁琐而重复的释放资源代码
     *
     * @return
     */
    public GraphQLSchema buildSchema() throws IOException {

        log.info("GroupProgressStatSchema#buildSchema ing");

        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry typeRegistry = null;


        URL url = this.getClass().getResource("");
        String protocol = url.getProtocol();
        if (JAR_TYPE.equals(protocol)) {

            @Cleanup InputStream in = this.getClass().getResourceAsStream("graphql/progressStat.graphqls");
            @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            typeRegistry = schemaParser.parse(reader);

        } else {
            File schemaFile = ResourceUtils.getFile("classpath:graphql/progressStat.graphqls");
            typeRegistry = schemaParser.parse(schemaFile);
        }


        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);

        return graphQLSchema;
    }
}
