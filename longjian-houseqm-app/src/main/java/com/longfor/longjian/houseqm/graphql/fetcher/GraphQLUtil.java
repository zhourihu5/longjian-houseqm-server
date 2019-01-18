package com.longfor.longjian.houseqm.graphql.fetcher;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import graphql.GraphQL;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.schema.GraphQLSchema;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static graphql.GraphQL.newGraphQL;


/**
 * @author lipeishuai
 * @date 2019/1/17 21:17
 */

@Component
public class GraphQLUtil {

    private static volatile GraphQL graphQL;

    @Autowired
    private GraphQLSchema schema;

    public GraphQL getGraphQL() {
        Cache<String, PreparsedDocumentEntry> cache = Caffeine.newBuilder().maximumSize(10_000).build();
        if (graphQL == null) {
            graphQL = newGraphQL(schema).preparsedDocumentProvider(cache::get).build();
        }
        return graphQL;
    }

    public static String getQueryString(String input) {
        JSONObject requestQuery = null;
        try {
            requestQuery = new JSONObject(input);
            return requestQuery.getString("query");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
