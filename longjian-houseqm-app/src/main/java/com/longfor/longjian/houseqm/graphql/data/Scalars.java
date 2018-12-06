package com.longfor.longjian.houseqm.graphql.data;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lipeishuai
 * @date 2018/12/4 20:46
 */
public class Scalars {


    public static final GraphQLScalarType DateField = new GraphQLScalarType("DateField", "A custom date scalar ", new Coercing() {
        @Override
        public String serialize(Object input) {
            // serialize the ZonedDateTime into string on the way out
            return ((Date)input).toString();
        }

        @Override
        public Object parseValue(Object input) {
            return serialize(input);
        }

        @Override
        public Date parseLiteral(Object input) {
            // parse the string values coming in
            if (input instanceof Timestamp) {
                return new Date(((Timestamp) input).getTime());
            } else if (input instanceof Date) {
                return ((Date)input);
            } else {
                return null;
            }
        }
    });


    static final GraphQLScalarType GraphQLDateField = new GraphQLScalarType("DateField", "Built-in GraphQLDateField", new Coercing<String, String>() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        private String convertImpl(Object input) {

            return sdf.format(input);
        }

        @Override
        public String serialize(Object input) {
            return convertImpl(input);
        }

        @Override
        public String parseValue(Object input) {
            return convertImpl(input);
        }

        @Override
        public String parseLiteral(Object input) {
            return convertImpl(input);
        }
    });



}
