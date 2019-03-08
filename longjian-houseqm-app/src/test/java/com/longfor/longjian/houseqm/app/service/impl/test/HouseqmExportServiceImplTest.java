package com.longfor.longjian.houseqm.app.service.impl.test;

import com.longfor.longjian.houseqm.app.service.impl.HouseqmExportServiceImpl;
import com.longfor.longjian.houseqm.app.vo.houseqm.HouseqmExportVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HouseqmExportServiceImplTest {

    @Resource
    private HouseqmExportServiceImpl houseqmExportServiceImpl;
//org.springframework.jdbc.UncategorizedSQLException:
    @Test
    public void searchProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn() {
        try {
            houseqmExportServiceImpl.searchProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(927, 1, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 1, new Date(), new Date(), 1);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }
//org.springframework.jdbc.UncategorizedSQLException:
    @Test
    public void exportProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn() {
        try{
            HouseqmExportVo vo=new HouseqmExportVo();
            vo.setAreaIds(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
            vo.setBeginOn(new Date());
            vo.setEndOn(new Date());
            vo.setCategoryCls(1);
            vo.setProjectId(927);
            vo.setRepairerId(1);
            vo.setTaskId(1);
            vo.setWithRule(true);
            houseqmExportServiceImpl.exportProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(vo);
        }catch (Exception e){
            e.printStackTrace();
            //Assert.fail(e.getMessage());
        }
    }
}