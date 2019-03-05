package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

// 获取启动类，加载配置，确定装载 Spring 程序的装载方法，它回去寻找 主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest(classes = {com.longfor.longjian.houseqm.Application.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class ExportFileRecordServiceImplTest {

    @Resource
    private com.longfor.longjian.houseqm.domain.internalservice.impl.ExportFileRecordServiceImpl exportFileRecordServiceImpl;

    @Test
    public void insertFull() {
        int testId = -1;
        try {
            ExportFileRecord efr = exportFileRecordServiceImpl.insertFull(1, 1, 1, 1, "test", "test", "test", 1, "test", new Date());
            //testId = efr.getId();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}