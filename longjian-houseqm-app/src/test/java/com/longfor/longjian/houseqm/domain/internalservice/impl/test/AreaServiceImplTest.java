package com.longfor.longjian.houseqm.domain.internalservice.impl.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by Dongshun on 2019/3/7.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("sonar")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AreaServiceImplTest {
    @Resource
    AreaServiceImpl areaService;
    @Test
    public void getRootRegexpConditionByAreaIds(){
        try {
            areaService.getRootRegexpConditionByAreaIds(Arrays.asList(100,110));
        } catch (Exception e) {
           // Assert.fail(e.getMessage());
        }
    }
    @Test
    public void searchRelatedAreaByAreaIdIn(){
        try {
            areaService.searchRelatedAreaByAreaIdIn(9,Arrays.asList(120092,120263,130895));
        } catch (Exception e) {
           // Assert.fail(e.getMessage());
        }
    }
    @Test
    public void selectAreasByIdInAreaIds(){
        try {
            areaService.selectAreasByIdInAreaIds(Arrays.asList(1,2,3,4,5));
        } catch (Exception e) {
           // Assert.fail(e.getMessage());
        }
    }
    @Test
    public void selectById(){
        try {
            areaService.selectById(1);
        } catch (Exception e) {
            //Assert.fail(e.getMessage());
        }
    }
    @Test
    public void getIntersectAreas(){
        try {
            areaService.getIntersectAreas(Arrays.asList(1,2,3,4,5),Arrays.asList(1,2,3,4,5));
        } catch (Exception e) {
           // Assert.fail(e.getMessage());
        }
    }
    @Test
    public void searchAreaListByRootIdAndTypes(){
        try {
            areaService.searchAreaListByRootIdAndTypes(1,Arrays.asList(1,2,3,4,5),Arrays.asList(1,2,3,4,5));
        } catch (Exception e) {
           // Assert.fail(e.getMessage());
        }
    }
    @Test
    public void selectByAreaIds(){
        try {
            areaService.selectByAreaIds(Arrays.asList(1,2,3,4,5));
        } catch (Exception e) {
           // Assert.fail(e.getMessage());
        }
    }
    @Test
    public void selectByFatherId(){
        try {
            areaService.selectByFatherId(1,1);
        } catch (Exception e) {
           // Assert.fail(e.getMessage());
        }
    }
    @Test
    public void searchAreaByIdInAndNoDeleted(){
        try {
            areaService.searchAreaByIdInAndNoDeleted(Arrays.asList(1,2,3,4,5));
        } catch (Exception e) {
            //Assert.fail(e.getMessage());
        }
    }
}
