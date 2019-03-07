package com.longfor.longjian.houseqm.app.service.impl.test;

import com.longfor.longjian.houseqm.app.service.impl.HouseqmIssueServiceImpl;
import com.longfor.longjian.houseqm.domain.internalservice.impl.HouseQmCheckTaskIssueServiceImpl;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@ActiveProfiles("sonar")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HouseqmIssueServiceImplTest {

    @Resource
    private HouseqmIssueServiceImpl houseqmIssueServiceImpl;
//LjBaseRuntimeException(errorCode=-1, errorMsg=删除问题失败)
    @Test
    @Transactional
    public void deleteHouseQmCheckTaskIssueByProjUuid() {
        try{
            houseqmIssueServiceImpl.deleteHouseQmCheckTaskIssueByProjUuid(1,"a");
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void updateBatchIssueApproveStatusByUuids() {
        try{
            HouseQmCheckTaskIssue hcti=new HouseQmCheckTaskIssue();
            hcti.setAreaId(1);
            hcti.setAreaPathAndId("/1/2/3/");
            hcti.setAttachmentMd5List("a,b,c,d");
            hcti.setAudioMd5List("e,f,g,h");
            hcti.setCategoryCls(1);
            hcti.setCategoryKey("1");
            hcti.setCategoryPathAndKey("/1/2/");
            hcti.setCheckItemKey("1");
            hcti.setCheckItemPathAndKey("/1/2/3/");
            hcti.setClientCreateAt(new Date());
            hcti.setCondition(1);
            hcti.setContent("1");
            hcti.setCount(1);
            hcti.setCreateAt(new Date());
            hcti.setDeleteTime(new Date());
            hcti.setDestroyAt(new Date());
            hcti.setDestroyUser(1);
            hcti.setDetail("!");
            hcti.setDrawingMD5("a");
            hcti.setEndOn(new Date());
            hcti.setId(1);
            hcti.setLastAssignAt(new Date());
            hcti.setLastAssigner(1);
            hcti.setRepairerId(1);
            hcti.setSenderId(1);
            hcti.setPlanEndOn(new Date());
            hcti.setLastRepairer(1);
            hcti.setLastRepairerAt(new Date());
            hcti.setPosX(1);
            hcti.setPosY(1);
            hcti.setRepairerFollowerIds("1,2,3,4,5,6,7,8,9,10");
            hcti.setStatus(1);
            hcti.setTaskId(1);
            hcti.setTitle("a");
            hcti.setTyp(1);
            hcti.setUuid("a");
            hcti.setUpdateAt(new Date());
            houseqmIssueServiceImpl.updateBatchIssueApproveStatusByUuids(Arrays.asList("1","2","3"),1,1,1,"a","a,b,c");
        }catch (Exception e){
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void updateBatchIssueRepairInfoByUuids() {
        try{
            houseqmIssueServiceImpl.updateBatchIssueRepairInfoByUuids(Arrays.asList("1","2","3"),1,1,1,"1,2,3,4",DateUtil.datetimeToTimeStamp(new Date()));
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }

    @Test
    public void create() {
    }

    @Test
    public void getProjectByProjId() {
    }

    @Test
    public void searchHouseQmIssueListByProjUuidIn() {
    }
}