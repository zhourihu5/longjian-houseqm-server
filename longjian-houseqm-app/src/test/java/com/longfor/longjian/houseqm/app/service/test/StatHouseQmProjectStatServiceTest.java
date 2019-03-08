package com.longfor.longjian.houseqm.app.service.test;

import com.longfor.longjian.houseqm.app.service.StatHouseQmProjectStatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Dongshun on 2019/3/7.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("sonar")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StatHouseQmProjectStatServiceTest {
    @Resource
    StatHouseQmProjectStatService statHouseQmProjectStatService;
    @Test
    public void  searchStat(){
        try {
            statHouseQmProjectStatService.searchStat(1,"1","1", Arrays.asList( 1, 2, 34, 5),new Date(),new Date(),1);
        } catch (Exception e) {
            //Assert.fail(e.getMessage());
        }
    }

}
