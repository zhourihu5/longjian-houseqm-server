package com.longfor.longjian.houseqm.servicetest.implTest;

import com.longfor.longjian.houseqm.Application;
import com.longfor.longjian.houseqm.app.req.IssueListDoActionReq;
import com.longfor.longjian.houseqm.app.req.bgtask.ExportBuildingExcelReq;
import com.longfor.longjian.houseqm.app.service.impl.IssueServiceImpl;
import com.longfor.longjian.houseqm.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IssueServiceImplTest {

    @Resource
    private IssueServiceImpl issueServiceImpl;

    @Test
    @Transactional
    public void updateIssueDetailByProjectAndUuid() {
        try{
            issueServiceImpl.updateIssueDetailByProjectAndUuid(9,927,"a",1,"123");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void exportExcel() {
        try{
            ExportBuildingExcelReq req=new ExportBuildingExcelReq();
            req.setChecker_id(1);
            req.setCondition(1);
            req.setIs_overdue(false);
            req.setRepairer_id(1);
            req.setTask_id(1);
            req.setType(1);
            req.setArea_ids("1,2,3,4,5,6,7,8,9,10");
            req.setCategory_cls(1);
            req.setCategory_key("3");
            req.setCheck_item_key("1");
            req.setCreate_on_begin("2018-01-01");
            req.setCreate_on_end("2018-02-01");
            req.setKey_word("a");
            req.setProject_id(927);
            req.setStatus_in("1");
            issueServiceImpl.exportExcel(9,req);
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void createTaskMap() {
        try{
            issueServiceImpl.createTaskMap(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void list() {
        try{
            IssueListDoActionReq req=new IssueListDoActionReq();
            req.set_overdue(false);
            req.setArea_ids("1,2,3,4,5,6,7,8,9");
            req.setCategory_cls(1);
            req.setCategory_key("1");
            req.setCheck_item_key("1");
            req.setChecker_id(1);
            req.setCondition(1);
            req.setCreate_on_begin("2018-10-01");
            req.setCreate_on_end("2018-10-02");
            req.setKey_word("a");
            req.setPage(1);
            req.setPage_size(10);
            req.setProject_id(1);
            req.setRepairer_id(1);
            req.setStatus_in("1");
            req.setTask_id(1);
            req.setType(1);
            issueServiceImpl.list(req);
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseQmCheckTaskIssueActionLogByIssueUuid() {
        try{
            issueServiceImpl.getHouseQmCheckTaskIssueActionLogByIssueUuid("89a3d67168c040f5b13a880556bb082c");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

 /*@Test
    @Transactional
    public void deleteHouseqmCheckTaskIssueByProjectAndUuid() {
        try{
            issueServiceImpl.deleteHouseqmCheckTaskIssueByProjectAndUuid(1,"460b19da386d4896857b6b1bdc6039cb");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }
*/
    @Test
    @Transactional
    public void updeteIssueDescByUuid() {
        try{
            issueServiceImpl.updeteIssueDescByUuid(927,"a",9,"a");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void updateIssuePlanEndOnByProjectAndUuid() {
        try{
            issueServiceImpl.updateIssuePlanEndOnByProjectAndUuid(927,"a",9, DateUtil.datetimeToTimeStamp(new Date()));
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void updateIssueApproveStatusByUuid() {
        try{
            issueServiceImpl.updateIssueApproveStatusByUuid(927,"a",9,1,"a");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

/*
    @Test
    @Transactional
    public void repairNotifyExport2() {
        try{
            issueServiceImpl.repairNotifyExport2(9,927,"1",new MockHttpServletResponse());
        }catch (Exception e){

           Assert.fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void repairReplyExport() {
        try{
            issueServiceImpl.repairNotifyExport(9,927,"1",new MockHttpServletResponse(),new MockHttpServletRequest());
        }catch (Exception e){

           Assert.fail(e.getMessage());
        }
    }
*/

    @Test
    public void getProjectSettingId() {
        try{
            issueServiceImpl.getProjectSettingId(927);
        }catch (Exception e){

           Assert.fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void updateIssueRepairInfoByProjectAndUuid() {
        try{
            issueServiceImpl.updateIssueRepairInfoByProjectAndUuid(9,1,"1,2,3,4,5,6,7,8,9",927,"a");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getDetailRepairLogByIssueUuid() {
        try{
            issueServiceImpl.getDetailRepairLogByIssueUuid("349d204213424acd93478097aa884539");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid() {
        try{
            issueServiceImpl.getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(9,3,"349d204213424acd93478097aa884539");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }

 /*   @Test
    @Transactional
    public void repairNotifyExport() {
        try{
            issueServiceImpl.repairNotifyExport(9,927,"a",new MockHttpServletResponse(),new MockHttpServletRequest());
        }catch (Exception e){

           Assert.fail(e.getMessage());
        }
    }
*/
    @Test
    @Transactional
    public void pushBaseMessage() {
        try{
            ArrayList<String> al=new ArrayList<>();
            al.addAll(Arrays.asList(new String[]{"1","2","3"}));
            issueServiceImpl.pushBaseMessage(1,al,"1","bvd");
        }catch (Exception e){
           Assert.fail(e.getMessage());
        }
    }
}