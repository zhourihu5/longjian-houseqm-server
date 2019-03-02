package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.util.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Dongshun on 2019/2/15.
 */
@Slf4j
public class DocumentHandler {
    //Configuration存储一些全局常量和常用设置
    private  static  Configuration configuration = new Configuration();
    private static final String ERROR="error:";
    private static final String UTF_8="utf-8";
    private static final String BASE_PACKAGE_PATH="/templates";
    //构造函数生成实例并设置编码
    public DocumentHandler() {
        configuration.setDefaultEncoding(UTF_8);
    }


    /**
     * 导出word文档，导出到本地
     * @param tempName，要使用的模板
     * @param dataMap，模板中变量数据
     * @param outFile，输出文档路径
     */
    public boolean exportDoc(String tempName, Map<?, ?> dataMap, File outFile) {
        boolean status = false;

        // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
        configuration.setClassForTemplateLoading(this.getClass(), BASE_PACKAGE_PATH);

        Template t = null;

        try{
            // tempName.ftl为要装载的模板
            t = configuration.getTemplate(tempName+".ftl");
            t.setEncoding(UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        try(Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), UTF_8))){
            t.process(dataMap, out);
            status = true;
        }catch(Exception e1) {
            log.error(ERROR,e1.getMessage());
        }

        return status;
    }

    /**
     * 导出word文档，响应到请求端
     * @param tempName，要使用的模板
     * @param docName，导出文档名称
     * @param dataMap，模板中变量数据
     * @param resp,HttpServletResponse
     */
    public boolean exportDoc(String tempName, String docName, Map<?, ?> dataMap, HttpServletResponse resp) {
        boolean status = false;
        if (resp != null) {
            resp.reset();
        }

        // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载。参数2为模板路径
        configuration.setClassForTemplateLoading(this.getClass(), BASE_PACKAGE_PATH);

        Template t = null;

        try {
            // tempName.ftl为要装载的模板
            t = configuration.getTemplate(tempName + ".ftl");
            t.setEncoding(UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // 输出文档路径及名称 ,以临时文件的形式导出服务器，再进行下载
        String name = "temp" + new Random().nextInt(100000) + ".doc";
        File outFile = new File(name);

        try(Writer out =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), UTF_8))) {
           t.process(dataMap, out);
            status = true;
        } catch (Exception e1) {
            log.error(ERROR,e1.getMessage());
        }

        if(resp==null){
            return false;
        }
        try ( InputStream fin = new FileInputStream(outFile);ServletOutputStream sos = resp.getOutputStream()){
            resp.setCharacterEncoding(UTF_8);
            resp.setContentType("application/msword");
            docName = new String(docName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            resp.setHeader("Content-disposition", "attachment;filename=" + docName + ".doc");
           byte[] buffer = new byte[512];
            int bytesToRead = -1;
            while ((bytesToRead = fin.read(buffer)) != -1) {
                sos.write(buffer, 0, bytesToRead);
            }
        } catch ( IOException e) {
            log.error(e.getMessage());
        } finally{
            try {
                Files.delete(Paths.get(outFile.toURI()));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return status;
    }
    //获得图片的base64码
    public static String getImageBase(String src){
        if (src == null || src == "") {
            return "";
        }
        File file = new File(src);
        if (!file.exists()) {
            return "";
        }
         byte[] data = null;
        try (InputStream in =new FileInputStream(file)){
            data = new byte[in.available()];
            int readCount=in.read(data);
            log.info("getImageBase:read bytes-"+readCount);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
    public static boolean exportWordBatch(HttpServletRequest request, HttpServletResponse response,List<Map<String, Object>> mapList, List<String> titleList, String ftlFile) {
        boolean status=false;
        File zipfile=null;
        File directory=null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        String str = DateUtil.getNowTimeStr("yyyy_MM_dd_hh_mm_ss");
        try {
            response .addHeader("Content-Disposition", "attachment;filename="+String.format("%s%s%s",  URLEncoder.encode("整改通知单_", UTF_8),str.replace("_", ""),".zip"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        configuration.setClassForTemplateLoading(DocumentHandler.class, BASE_PACKAGE_PATH);
        //根据当前时间和用户id创建临时目录
        String path=request.getRealPath("/resources/word/"+String.format("%s%s",  "整改通知单_",str.replace("_", "")));
        zipfile=new File(path+"zip.zip");
        try (InputStream fin=new FileInputStream(zipfile);ServletOutputStream out = response.getOutputStream()){
            Template freemarkerTemplate = configuration.getTemplate(ftlFile,"UTF-8");
            directory=new File(path);
            directory.mkdirs();
            for(int i=0;i<mapList.size();i++) {
                Map<String, Object> map=mapList.get(i);
                String title=titleList.get(i);
                // 调用工具类的createDoc方法在临时目录下生成Word文档
               createDoc(map,freemarkerTemplate,directory.getPath()+"/"+title+".doc");
            }
            //压缩目录
            ZipUtils.createZip(path, path+"zip.zip");
            //根据路径获取刚生成的zip包文件

            byte[] buffer = new byte[1024]; // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
            status=true;

        }catch (Exception e) {
            log.error(e.getMessage());
        }
        finally {
            try {
                Files.delete(Paths.get(zipfile.toURI()));
                if (directory!=null) {
                    //递归删除目录及目录下文件
                    ZipUtils.deleteFile(directory);
                }
            } catch (Exception e2) {
                log.error(ERROR,e2.getMessage());
            }

        }
        return status;
    }

    //生成word文档方法
    private static File createDoc(Map<?, ?> dataMap, Template template,String filename) {
        File f = new File(filename);
        Template t = template;
        try (FileOutputStream fos=new FileOutputStream(f);Writer w =new OutputStreamWriter(fos, UTF_8)){
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            //不要偷懒写成下面酱紫: 否则无法关闭fos流，打zip包时存取被拒抛异常
            t.process(dataMap, w);
        } catch (Exception ex) {
            log.error(ERROR,ex.getMessage());

        }

        return f;
    }
}
