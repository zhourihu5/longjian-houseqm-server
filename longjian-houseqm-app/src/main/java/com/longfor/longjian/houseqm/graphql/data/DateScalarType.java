package com.longfor.longjian.houseqm.graphql.data;


import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;

/**
 * @author lipeishuai
 * @date 2018/12/6 16:09
 */
@Component
public class DateScalarType extends GraphQLScalarType {

    private final static Logger logger = LoggerFactory.getLogger(DateScalarType.class);

    public DateScalarType() {
        super("DateField", "A Date type", new Coercing<Date, String>() {

            @Override
            public String serialize(Object input) {
                if (input instanceof Date) {
                    return createIsoDateFormat().format((Date) input);
                }
                return null;
            }

            @Override
            public Date parseValue(Object input) {
                if (input instanceof Date) {
                    return (Date) input;
                } else if (input instanceof String) {
                    try {
                        Date date = createIsoDateFormat().parse((String) input);
                        return date;
                    } catch (ParseException e) {
                        logger.error(format("Could not parse date from String '%s': %s", input, e.getLocalizedMessage()), e);
                    }
                }
                return null;
            }

            @Override
            public Date parseLiteral(Object input) {
                throw new UnsupportedOperationException("ParseLiteral in DateScalarType not implemented yet");
            }
        });
    }

    private static SimpleDateFormat createIsoDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public String getDescription() {
        return "A Type representing a date (without time, only a day)";
    }
}