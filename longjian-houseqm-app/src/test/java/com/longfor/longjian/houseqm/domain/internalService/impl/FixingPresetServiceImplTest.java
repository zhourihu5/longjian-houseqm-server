package com.longfor.longjian.houseqm.domain.internalservice.impl;

import org.apache.http.client.utils.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

// 获取启动类，加载配置，确定装载 Spring 程序的装载方法，它回去寻找 主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest(classes = {com.longfor.longjian.houseqm.Application.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class FixingPresetServiceImplTest {

    @Resource
    private FixingPresetServiceImpl fixingPresetServiceImpl;

    @Test
    public void selectByProAndIdAndUIdsAndminutes() {
        try{
            fixingPresetServiceImpl.selectByProAndIdAndUIdsAndminutes(1,1,20);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectByProAndIdAndUpdate() {
        try{
            fixingPresetServiceImpl.selectByProAndIdAndUpdate(1,1,20, DateUtils.parseDate("0001-01-01",new String[]{"yyyy-MM-dd"}));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}