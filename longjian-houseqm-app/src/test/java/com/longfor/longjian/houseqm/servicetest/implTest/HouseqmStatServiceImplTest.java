package com.longfor.longjian.houseqm.servicetest.implTest;

import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.app.service.impl.HouseqmStatServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HouseqmStatServiceImplTest {

    @Resource
    private HouseqmStatServiceImpl houseqmStatServiceImpl;

    @Test
    public void searchInspectionAreaIdsByConditions() {
        try{
            houseqmStatServiceImpl.searchInspectionAreaIdsByConditions(927,1,1,1,1);
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

/*
    @Test
    public void searchRepossessInspectionAreaIdsByConditions() {
        try{
            houseqmStatServiceImpl.searchRepossessInspectionAreaIdsByConditions(927,1,1,1,1,new Date(),new Date());
        }catch (Exception e){

           Assert.fail(e.getMessage());
        }
    }
*/

    @Test
    public void filterAreaPathListByRootAreaId() {
        try{
            houseqmStatServiceImpl.filterAreaPathListByRootAreaId(1, Arrays.asList(new String[]{"/1/","/1/2/3/"}));
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getRepossessAreaPathListByTaskIdAndStatusesAndClientUpdateAt() {
        try{
            houseqmStatServiceImpl.getRepossessAreaPathListByTaskIdAndStatusesAndClientUpdateAt(1,Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,10}),new Date(),new Date());
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void formatFenhuHouseInspectionStatusInfoByAreaIds() {
        try{
            houseqmStatServiceImpl.formatFenhuHouseInspectionStatusInfoByAreaIds(1,1,Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,10}));
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchHouseQmCheckTaskIssueMapByTaskIdAreaIds() {
        try{
            houseqmStatServiceImpl.searchHouseQmCheckTaskIssueMapByTaskIdAreaIds(1,1,Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,10}));
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn() {
        try{
            houseqmStatServiceImpl.searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(927,1,1,new Date(),new Date());
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getAreaIssueTypeStatByProjectIdAreaIdCategoryCls() {
        try{
            houseqmStatServiceImpl.getAreaIssueTypeStatByProjectIdAreaIdCategoryCls(927,1,1);
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchRepaireIssueStatusStatByProjTaskIdBetweenTime() {
        try{
            houseqmStatServiceImpl.searchRepaireIssueStatusStatByProjTaskIdBetweenTime(927,1,new Date(),new Date());
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchCheckerIssueStatusStatByProjTaskIdBetweenTime() {
        try{
            houseqmStatServiceImpl.searchCheckerIssueStatusStatByProjTaskIdBetweenTime(927,1,new Date(),new Date());
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchCheckerIssueStatisticByProjIdAndTaskId() {
        try{
            houseqmStatServiceImpl.searchCheckerIssueStatisticByProjIdAndTaskId(927,Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,10}));
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchTaskSituationDailyByProjTaskIdInOnPage() {
        try{
            houseqmStatServiceImpl.searchTaskSituationDailyByProjTaskIdInOnPage(927,Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,10}),1,10);
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getInspectTaskStatByProjTaskId() {
        try{
            houseqmStatServiceImpl.getInspectTaskStatByProjTaskId(927,1);
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchAreasByProjTaskIdTyp() {
        try{
            houseqmStatServiceImpl.searchAreasByProjTaskIdTyp(927,1,1);
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn() {
        try{
            houseqmStatServiceImpl.searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(927,1,Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,10}));
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }
}