package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.longjian.houseqm.domain.internalService.impl.AreaServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Arrays;

import static org.junit.Assert.*;
// 获取启动类，加载配置，确定装载 Spring 程序的装载方法，它回去寻找 主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest(classes = {com.longfor.longjian.houseqm.Application.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class AreaServiceImplTest {

    @Resource
    private AreaServiceImpl areaServiceImpl;

    @Test
    public void getRootRegexpConditionByAreaIds() {
        try {
            areaServiceImpl.getRootRegexpConditionByAreaIds(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchRelatedAreaByAreaIdIn() {
        try {
            areaServiceImpl.searchRelatedAreaByAreaIdIn(1,Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectAreasByIdInAreaIds() {
        try {
            areaServiceImpl.selectAreasByIdInAreaIds(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectById() {
        try {
            areaServiceImpl.selectById(1);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getIntersectAreas() {
        try {
            areaServiceImpl.getIntersectAreas(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchAreaListByRootIdAndTypes() {
        try {
            areaServiceImpl.searchAreaListByRootIdAndTypes(1,Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectByAreaIds() {
        try {
            areaServiceImpl.selectByAreaIds(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectByFatherId() {
        try {
            areaServiceImpl.selectByFatherId(1,1);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchAreaByIdInAndNoDeleted() {
        try {
            areaServiceImpl.searchAreaByIdInAndNoDeleted(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}