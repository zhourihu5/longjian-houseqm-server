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
@RequestMapping("/pic")
      public void Pic () throws Exception {
        String urlString= "http://172.17.96.90:8082/lhdata/pictures/2019-01-02/1546398967641194269";
    String filename="1546398967641194269.png";
    String savePath="/templates/Pic";
    download(urlString,filename,savePath);
      }
    public static void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }
}
