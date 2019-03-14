package com.longfor.longjian.houseqm.servicetest;

import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.app.service.HouseqmStaticService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Dongshun on 2019/3/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HouseqmStaticServiceTest {
    @Resource
    HouseqmStaticService houseqmStaticService;
    @Test
    public  void  searchHouseQmCheckTaskByProjCategoryCls(){
        try {
            houseqmStaticService.searchHouseQmCheckTaskByProjCategoryCls(1,1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  getHouseQmCheckTaskHouseStatByTaskId(){
        try {
            houseqmStaticService.getHouseQmCheckTaskHouseStatByTaskId(1,1,1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  taskBuildingList(){
        try {
            houseqmStaticService.taskBuildingList(1,1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  pSelectByFatherId(){
        try {
            houseqmStaticService.pSelectByFatherId(1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  getIssueMinStatusMapByTaskIdAndAreaId(){
        try {
            houseqmStaticService.getIssueMinStatusMapByTaskIdAndAreaId(1,1,true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  splitToIdsComma(){
        try {
            houseqmStaticService.splitToIdsComma("1,23,4",",");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  getRepossessionTasksStatusInfo(){
        try {
            houseqmStaticService.getRepossessionTasksStatusInfo(1, Arrays.asList(1,2,3,4),1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  getHasIssueTaskCheckedAreaPathListByTaskId(){
        try {
            houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(1,true,Arrays.asList(1,2,3,4),1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  searchTargetAreaByTaskId(){
        try {
            houseqmStaticService.searchTargetAreaByTaskId(1,1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  searchUserInHouseQmCheckTaskByUserIdRoleType(){
        try {
            houseqmStaticService.searchUserInHouseQmCheckTaskByUserIdRoleType(1,1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  searchHouseQmCheckTaskByProjCategoryClsIn(){
        try {
            houseqmStaticService.searchHouseQmCheckTaskByProjCategoryClsIn(1,Arrays.asList(1,23,43));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(){
        try {
            houseqmStaticService.searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(1,1,Arrays.asList(1,2,3,4,5));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public  void  checkRootAreaIntersectAreas(){
        HashMap<Integer, String> map = Maps.newHashMap();
        map.put(1,"1");
        map.put(2,"1");
        map.put(3,"1");
        try {
            houseqmStaticService.checkRootAreaIntersectAreas(map,1, Arrays.asList(1,2,3));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
