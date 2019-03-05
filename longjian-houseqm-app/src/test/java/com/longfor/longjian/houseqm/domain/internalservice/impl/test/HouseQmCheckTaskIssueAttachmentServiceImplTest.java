package com.longfor.longjian.houseqm.domain.internalService.impl.test;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueAttachment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// 获取启动类，加载配置，确定装载 Spring 程序的装载方法，它回去寻找 主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest(classes = {com.longfor.longjian.houseqm.Application.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
@Transactional//这个非常关键，如果不加入这个注解配置，事务控制就会完全失效！
//这里的事务关联到配置文件中的事务控制器（transactionManager = "transactionManager"），同时//指定自动回滚（defaultRollback = true）。这样做操作的数据才不会污染数据库！
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class HouseQmCheckTaskIssueAttachmentServiceImplTest {

    @Resource
    private HouseQmCheckTaskIssueAttachmentServiceImpl houseQmCheckTaskIssueAttachmentServiceImpl;

    @Test
    public void inseretBatch() {
        try {
            HouseQmCheckTaskIssueAttachment ca = new HouseQmCheckTaskIssueAttachment();
            ca.setIssueUuid("1");
            ca.setAttachmentType(1);
            ca.setClientCreateAt(new Date());
            ca.setProjectId(1);
            ca.setPublicType(1);
            ca.setStatus(1);
            ca.setUserId(1);
            ca.setCreateAt(new Date());
            ca.setUpdateAt(new Date());
            ca.setTaskId(1);
            ca.setMd5("1");
            houseQmCheckTaskIssueAttachmentServiceImpl.inseretBatch(Arrays.asList(ca));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void deleteByIssueUuidMd5() {
        try {
            houseQmCheckTaskIssueAttachmentServiceImpl.deleteByIssueUuidMd5("1", "1");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void searchByIssueUuid() {
        try {
            Set<String> params = new HashSet<>();
            params.add("1");
            params.add("2");
            houseQmCheckTaskIssueAttachmentServiceImpl.searchByIssueUuid(params);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectByissueUuidAnduserIdAndpublicTypeAndattachmentTypeAndNotDel() {
        try {
            houseQmCheckTaskIssueAttachmentServiceImpl.selectByissueUuidAnduserIdAndpublicTypeAndattachmentTypeAndNotDel("1", 1, 1, 1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectByIssueUuidAndpublicTypeAndattachmentTypeAndNotDel() {
        try {
            houseQmCheckTaskIssueAttachmentServiceImpl.selectByIssueUuidAndpublicTypeAndattachmentTypeAndNotDel("1", 1, 1);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void add() {
        try {
            HouseQmCheckTaskIssueAttachment ca = new HouseQmCheckTaskIssueAttachment();
            ca.setIssueUuid("1");
            ca.setAttachmentType(1);
            ca.setClientCreateAt(new Date());
            ca.setProjectId(1);
            ca.setPublicType(1);
            ca.setStatus(1);
            ca.setUserId(1);
            ca.setCreateAt(new Date());
            ca.setUpdateAt(new Date());
            ca.setTaskId(1);
            ca.setMd5("1");
            houseQmCheckTaskIssueAttachmentServiceImpl.add(ca);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectByMd5AndNotDel() {
        try {
            houseQmCheckTaskIssueAttachmentServiceImpl.selectByMd5AndNotDel("1");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}