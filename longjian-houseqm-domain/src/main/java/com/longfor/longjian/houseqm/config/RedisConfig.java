package com.longfor.longjian.houseqm.config;

import com.longfor.gaia.gfs.data.redis.DynamicRedisProvider;
import com.longfor.gaia.gfs.data.redis.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author lipeishuai
 * @date 2018-11-11 18:13
 */
@Configuration
public class RedisConfig {

    @Resource
    private DynamicRedisProvider dynamicRedisProvider;

    @Bean(name = "demoRedis")
    public RedisTemplate demoRedis() {
        StringRedisTemplate template = new StringRedisTemplate(dynamicRedisProvider.loadRedis().get("demoRedis"));
        JacksonSerializer.setJacksonSerializer(template);
        return template;
    }

}
