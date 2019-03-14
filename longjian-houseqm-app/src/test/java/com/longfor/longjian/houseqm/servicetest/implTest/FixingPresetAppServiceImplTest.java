package com.longfor.longjian.houseqm.servicetest.implTest;

import com.longfor.longjian.common.util.DateUtil;
import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.app.service.impl.FixingPresetAppServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FixingPresetAppServiceImplTest {

    @Resource
    private FixingPresetAppServiceImpl fixingPresetAppServiceImpl;
    @Test
    public void appGetFixingPreset() {
        try{
            fixingPresetAppServiceImpl.appGetFixingPreset(927,1000, DateUtil.dateToTimestamp(new Date()),10);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}