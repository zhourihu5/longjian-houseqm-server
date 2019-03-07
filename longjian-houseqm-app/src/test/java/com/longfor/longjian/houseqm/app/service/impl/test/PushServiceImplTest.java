package com.longfor.longjian.houseqm.app.service.impl.test;

import com.longfor.longjian.houseqm.app.service.impl.PushServiceImpl;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.HouseQmCheckTaskIssueVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@ActiveProfiles("sonar")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PushServiceImplTest {

    @Resource
    private PushServiceImpl pushServiceImpl;

    @Test
    @Transactional
    public void sendUPushByIssues() {
        try{
            List<HouseQmCheckTaskIssueVo> vos=new ArrayList<>();
            HouseQmCheckTaskIssueVo vo=new HouseQmCheckTaskIssueVo();
            vo.setEndOn(new Date());
            pushServiceImpl.sendUPushByIssues(vos);
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void sendUPush() {
    }
}