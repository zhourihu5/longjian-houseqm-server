package com.longfor.longjian.houseqm.app.test;

import com.longfor.longjian.houseqm.util.DateUtil;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.http.util.EntityUtils;
import org.springframework.web.util.WebUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Dongshun on 2019/2/15.
 */
@Slf4j
public class DocumentHandler {
    //Configuration存储一些全局常量和常用设置
    private  static  Configuration configuration = null;

    //构造函数生成实例并设置编码
    public DocumentHandler() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
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
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");

        Template t = null;

        try{
            // tempName.ftl为要装载的模板
            t = configuration.getTemplate(tempName+".ftl");
            t.setEncoding("utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        //Writer out = null;
        try(Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"))){
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            t.process(dataMap, out);
            status = true;
        }catch(Exception e1) {
            log.error(e1.getMessage());
        }

        /*try{
            t.process(dataMap, out);
            out.close();
        }catch(TemplateException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

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
        //ServletOutputStream sos = null;
        //InputStream fin = null;
        if (resp != null) {
            resp.reset();
        }

        // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载。参数2为模板路径
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");

        Template t = null;

        try {
            // tempName.ftl为要装载的模板
            t = configuration.getTemplate(tempName + ".ftl");
            t.setEncoding("utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // 输出文档路径及名称 ,以临时文件的形式导出服务器，再进行下载
        String name = "temp" + (int) (Math.random() * 100000) + ".doc";
        File outFile = new File(name);

        //Writer out = null;
        try(Writer out =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"))) {
           // out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            t.process(dataMap, out);
            status = true;
        } catch (Exception e1) {
            log.error(e1.getMessage());
        }

        /*try {
            t.process(dataMap, out);
            out.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if(resp==null){
            return false;
        }
        try ( InputStream fin = new FileInputStream(outFile);ServletOutputStream sos = resp.getOutputStream()){
            //fin = new FileInputStream(outFile);
            if(resp!=null) {
                resp.setCharacterEncoding("utf-8");
                resp.setContentType("application/msword");
                docName = new String(docName.getBytes("UTF-8"), "ISO-8859-1");
                resp.setHeader("Content-disposition", "attachment;filename=" + docName + ".doc");
            }
            byte[] buffer = new byte[512];
            int bytesToRead = -1;
            while ((bytesToRead = fin.read(buffer)) != -1) {
                sos.write(buffer, 0, bytesToRead);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }finally{
            if (outFile != null) {
                boolean delete = outFile.delete();
                if(!delete){
                   log.info("文件删除失败");
                }
            }
        }
        // 文档下载
        //resp.setCharacterEncoding("utf-8");
       // resp.setContentType("application/msword");
        /*try {
            docName = new String(docName.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        resp.setHeader("Content-disposition", "attachment;filename=" + docName + ".doc");
        try {
            sos = resp.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       /* byte[] buffer = new byte[512]; // 缓冲区
        int bytesToRead = -1;
        // 通过循环将读入的Word文件的内容输出到浏览器中
        try {
            while ((bytesToRead = fin.read(buffer)) != -1) {
                sos.write(buffer, 0, bytesToRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null)
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (sos != null)
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (outFile != null)
                outFile.delete(); // 删除临时文件
        }*/

        return status;
    }
    //获得图片的base64码
    public static String getImageBase(String src) throws Exception {
        if (src == null || src == "") {
            return "";
        }
        File file = new File(src);
        if (!file.exists()) {
            return "";
        }
        //InputStream in = null;
        byte[] data = null;
        try (InputStream in =new FileInputStream(file)){
            //in = new FileInputStream(file);
            data = new byte[in.available()];
            int readCount=in.read(data);
            log.info("getImageBase:read bytes-"+readCount);
            in.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
    public static boolean exportWordBatch(HttpServletRequest request, HttpServletResponse response,List<Map<String, Object>> mapList, List<String> titleList, String ftlFile) {
        boolean status=false;
        File file = null;
        File zipfile=null;
        File directory=null;
        InputStream fin = null;
        ServletOutputStream out = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        String str = DateUtil.getNowTimeStr("yyyy_MM_dd_hh_mm_ss");
        try {
            response .addHeader("Content-Disposition", "attachment;filename="+String.format("%s%s%s",  URLEncoder.encode("整改通知单_", "utf-8"),str.replace("_", ""),".zip"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        configuration.setClassForTemplateLoading(DocumentHandler.class, "/templates");
        try {
            Template freemarkerTemplate = configuration.getTemplate(ftlFile,"UTF-8");
            out = response.getOutputStream();
            //根据当前时间和用户id创建临时目录
            String path=request.getRealPath("/resources/word/"+String.format("%s%s",  "整改通知单_",str.replace("_", "")));
            directory=new File(path);
            directory.mkdirs();
            for(int i=0;i<mapList.size();i++) {
                Map<String, Object> map=mapList.get(i);
                String title=titleList.get(i);
                // 调用工具类的createDoc方法在临时目录下生成Word文档
                file = createDoc(map,freemarkerTemplate,directory.getPath()+"/"+title+".doc");
            }
            //压缩目录
            /*  String.format("%s%s", "ds",str.replace("_", ""))+*/
            ZipUtils.createZip(path, path+"zip.zip");
            //根据路径获取刚生成的zip包文件
            zipfile=new File(path+"zip.zip");
            fin=new FileInputStream(zipfile);
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
                if (fin!=null) fin.close();
                if (out!=null) out.close();
                if (zipfile!=null) {
                    boolean b = zipfile.delete();
                    if(b){
                        log.info("删除成功");
                    }
                }
                if (directory!=null) {
                    //递归删除目录及目录下文件
                    ZipUtils.deleteFile(directory);
                }
            } catch (Exception e2) {
                log.error(e2.getMessage());
            }

        }
        return status;
    }

    //生成word文档方法
    private static File createDoc(Map<?, ?> dataMap, Template template,String filename) {
        File f = new File(filename);
        Template t = template;
        //Writer w =null;
        //FileOutputStream fos=null;
        try (FileOutputStream fos=new FileOutputStream(f);Writer w =new OutputStreamWriter(fos, "utf-8")){
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            //fos=new FileOutputStream(f);
            //w = new OutputStreamWriter(fos, "utf-8");
            //不要偷懒写成下面酱紫: 否则无法关闭fos流，打zip包时存取被拒抛异常
            //w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
        } catch (Exception ex) {
            log.error(ex.getMessage());

        }
        /*finally {
            try {
                fos.close();
                w.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/
        return f;
    }
}
