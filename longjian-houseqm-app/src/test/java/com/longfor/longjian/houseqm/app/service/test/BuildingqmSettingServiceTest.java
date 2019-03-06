package com.longfor.longjian.houseqm.app.service.test;

import com.longfor.longjian.houseqm.app.service.BuildingqmSettingService;
import com.longfor.longjian.houseqm.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Date;

@RunWith(SpringRunner.class)
@ActiveProfiles("sonar")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BuildingqmSettingServiceTest {

    @Resource
    private BuildingqmSettingService buildingqmSettingService;


    @Test
    public void getIssuefiledSetting() {
        try{
            buildingqmSettingService.getIssuefiledSetting("1,2,3,4,5,6,7,8,9,10", DateUtil.datetimeToTimeStamp(new Date()));
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}