package com.longfor.longjian.houseqm.domain.internalservice.impl;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BuildingqmCheckQuestionServiceImplTest {
    @Resource
    BuildingqmCheckQuestionServiceImpl buildingqmCheckQuestionService;


            @Test
            public void searchAreaByIdInAndNoDeleted(){
                try {
                    buildingqmCheckQuestionService.selectByProjectId(1);
                } catch (Exception e) {
                    //Assert.fail(e.getMessage());
                }
            }
    @Test
    public void findProjectIdsAndModuleId(){
        try {
            buildingqmCheckQuestionService.findProjectIdsAndModuleId(Arrays.asList(1,2,3,4,5));
        } catch (Exception e) {
            //Assert.fail(e.getMessage());
        }
    }
}
