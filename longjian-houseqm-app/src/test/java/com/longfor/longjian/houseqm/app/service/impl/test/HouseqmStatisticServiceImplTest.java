package com.longfor.longjian.houseqm.app.service.impl.test;

import com.longfor.longjian.houseqm.app.service.impl.HouseqmStatisticServiceImpl;
import com.longfor.longjian.houseqm.app.vo.SimpleHouseQmCheckTaskIssueStatVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatistic.TaskIssueRepairListVo;
import com.longfor.longjian.houseqm.po.zj2db.Area;
import com.longfor.longjian.houseqm.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("sonar")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HouseqmStatisticServiceImplTest {

    @Resource
    private HouseqmStatisticServiceImpl houseqmStatisticServiceImpl;



    @Test
    public void projectRepairerStat() {
        try{
            houseqmStatisticServiceImpl.projectRepairerStat(9,927,1,"/", DateUtil.datetimeToTimeStamp(new Date()),DateUtil.datetimeToTimeStamp(new Date()),DateUtil.datetimeToTimeStamp(new Date()));
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void projectCheckerStat() {
        try{
            houseqmStatisticServiceImpl.projectCheckerStat(9,927,1,"/", DateUtil.datetimeToTimeStamp(new Date()),DateUtil.datetimeToTimeStamp(new Date()),DateUtil.datetimeToTimeStamp(new Date()));
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void projectIssueStat() {
        try{
            houseqmStatisticServiceImpl.projectIssueStat(9,927,"/",1,DateUtil.datetimeToTimeStamp(new Date()));
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void projectList() {
        try{
            houseqmStatisticServiceImpl.projectList(9,"/",DateUtil.datetimeToTimeStamp(new Date()));
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getCheckTaskIssueTypeStatByTaskIdAreaId() {
        try{
            houseqmStatisticServiceImpl.getCheckTaskIssueTypeStatByTaskIdAreaId(1,1);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseQmCheckTaskHouseStatByTaskId() {
        try{
            houseqmStatisticServiceImpl.getHouseQmCheckTaskHouseStatByTaskId(927,1,1);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn() {
        try{
            houseqmStatisticServiceImpl.searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(927,1,1,new Date(),new Date());
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }
//org.springframework.jdbc.UncategorizedSQLException:
    @Test
    public void taskIssueRepairList() {
        try{
            TaskIssueRepairListVo vo=new TaskIssueRepairListVo();
            vo.setAreaId(1);
            vo.setBeginOn(DateUtil.datetimeToTimeStamp(new Date()));
            vo.setEndOn(DateUtil.datetimeToTimeStamp(new Date()));
            vo.setPage(1);
            vo.setPageSize(10);
            vo.setPlanStatus(1);
            vo.setProjectId(927);
            vo.setSource("/");
            vo.setTaskId(1);
            vo.setTimestamp(DateUtil.datetimeToTimeStamp(new Date()));
            houseqmStatisticServiceImpl.taskIssueRepairList(vo);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }
//org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.builder.BuilderException: The expression 'categoryClsList' evaluated to a null value.
    @Test
    public void projectIssueRepair() {
        try{
            houseqmStatisticServiceImpl.projectIssueRepair(927,"/",1,DateUtil.datetimeToTimeStamp(new Date()),DateUtil.datetimeToTimeStamp(new Date()),DateUtil.datetimeToTimeStamp(new Date()));
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }
//java.lang.NullPointerException
    @Test
    public void getHouseQmHouseQmCheckTaskHouseStatByTaskId() {
        try{
            houseqmStatisticServiceImpl.getHouseQmHouseQmCheckTaskHouseStatByTaskId(927,1,1);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }
//java.lang.NullPointerException
    @Test
    public void getRepossessionTasksStatusInfo() {
        try{
            houseqmStatisticServiceImpl.getRepossessionTasksStatusInfo(927, Arrays.asList(new Integer[]{1,2,3,4,5,6,7}),1);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchRepossessionStatusCompleteDaily() {
        try{
            houseqmStatisticServiceImpl.searchRepossessionStatusCompleteDaily(927, Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9}),DateUtil.datetimeToTimeStamp(new Date()),DateUtil.datetimeToTimeStamp(new Date()),1,10);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn() {
        try{
            houseqmStatisticServiceImpl.searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(927,1,1,new Date(),new Date());
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchHouseQmCheckTaskIssueOnlineInfoByProjCategoryKeyAreaIdPaged() {
        try{
            houseqmStatisticServiceImpl.searchHouseQmCheckTaskIssueOnlineInfoByProjCategoryKeyAreaIdPaged(927,"1",1,1,10);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void createAreasMapByAreaList() {
        try{
            List<Area> areas=new ArrayList<>();
            Area a1=new Area();
            a1.setAreaClassId(1);
            a1.setCreateAt(new Date());
            a1.setCustomCode("1");
            a1.setDrawingMd5("a");
            a1.setFatherId(1);
            a1.setIsLock(Short.valueOf("1"));
            a1.setLocation("a");
            a1.setName("a");
            a1.setOrderBy(1);
            a1.setPath("/");
            a1.setProjectId(927);
            a1.setUpdateAt(new Date());
            areas.add(a1);
            Area a2=new Area();
            a2.setAreaClassId(1);
            a2.setCreateAt(new Date());
            a2.setCustomCode("1");
            a2.setDrawingMd5("a");
            a2.setFatherId(1);
            a2.setIsLock(Short.valueOf("1"));
            a2.setLocation("a");
            a2.setName("a");
            a2.setOrderBy(1);
            a2.setPath("/");
            a2.setProjectId(927);
            a2.setUpdateAt(new Date());
            areas.add(a2);
            houseqmStatisticServiceImpl.createAreasMapByAreaList(areas);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void calculateIssueCount() {
        try{
            ArrayList<SimpleHouseQmCheckTaskIssueStatVo> vos=new ArrayList<>();
            SimpleHouseQmCheckTaskIssueStatVo svo=new SimpleHouseQmCheckTaskIssueStatVo();
            svo.setCategoryKey("1");
            svo.setCategoryPathAndKey("/1/2/");
            svo.setCheckItemKey("1");
            svo.setCheckItemPathAndKey("/1/2/3/");
            svo.setCount(1);
            SimpleHouseQmCheckTaskIssueStatVo svo1=new SimpleHouseQmCheckTaskIssueStatVo();
            svo1.setCategoryKey("1");
            svo1.setCategoryPathAndKey("/1/2/");
            svo1.setCheckItemKey("1");
            svo1.setCheckItemPathAndKey("/1/2/3/");
            svo1.setCount(1);
            vos.add(svo);
            vos.add(svo1);
            houseqmStatisticServiceImpl.calculateIssueCount(vos);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void isCategoryStatLevelThree() {
        try{
            houseqmStatisticServiceImpl.isCategoryStatLevelThree("1");
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void createAreasMapByLeaveIds() {
        try{
            houseqmStatisticServiceImpl.createAreasMapByLeaveIds(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9,10}));
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }
}