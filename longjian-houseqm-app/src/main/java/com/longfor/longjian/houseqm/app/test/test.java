package com.longfor.longjian.houseqm.app.test;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.app.utils.FileUtil;
import com.longfor.longjian.houseqm.dao.zj2db.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.util.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by Dongshun on 2019/2/18.
 */
@RestController
@RequestMapping("dds")
public class test {
    //导出到本地
    @RequestMapping("/doc")
    public void exportWordTest1(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        //这里的键要对应模板中的变量
        dataMap.put("name", "1018-工程");
        dataMap.put("buwei", "八期");
        dataMap.put("neirong", "ccc");
        dataMap.put("content", "ddd");
        List<String > list = Lists.newArrayList();
        list.add(DocumentHandler.getImageBase("d:/1.png"));
        list.add(DocumentHandler.getImageBase("d:/1.png"));
        dataMap.put("image",  list);

        Map<String, Object> dataMaps = new HashMap<>();
        //这里的键要对应模板中的变量
        dataMaps.put("name", "1018-工程");
        dataMaps.put("buwei", "八期");
        dataMaps.put("neirong", "ccc");
        dataMaps.put("content", "ddd");
        List<String > lists = Lists.newArrayList();
        list.add(DocumentHandler.getImageBase("d:/1.png"));
        list.add(DocumentHandler.getImageBase("d:/1.png"));
        dataMaps.put("image",  lists);
        List<Map<String,Object>> objects = Lists.newArrayList();
        objects.add(dataMap);
        objects.add(dataMaps);
        List<String > numbers = Lists.newArrayList();
        numbers.add("1");
        numbers.add("2");
        System.out.println(dataMap.toString());
        //单文件导出
/*        boolean b = new DocumentHandler().exportDoc("notify_template","导出问题通知单", dataMap, resp);

        System.out.println(b);*/
       //多文件导出
         new DocumentHandler().exportWordBatch(req,resp,objects,numbers,"notify_template.ftl");
    }
}
