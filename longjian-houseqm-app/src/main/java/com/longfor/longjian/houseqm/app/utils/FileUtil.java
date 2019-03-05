package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.common.exception.CommonRuntimeException;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理工具类
 *
 * @author zkm
 * @date 2018/12/28 9:53
 */
@Slf4j
public class FileUtil {

    private FileUtil(){

    }
    private  static final String READ_FILE_FAILED="读取文件失败";
    /**
     * 读取文件并返回文件内容
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        StringBuilder buffer = new StringBuilder();
        try (InputStream is =new FileInputStream(filePath); BufferedReader reader =new BufferedReader(new InputStreamReader(is))){
             // 每行的数据
            String line;
            line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\n");
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            log.info("文件不存在, filePath=", filePath, e);
            throw new CommonRuntimeException("文件不存在, filePath=" + filePath);
        } catch (IOException e) {
            log.info(READ_FILE_FAILED, e);
            throw new CommonRuntimeException(READ_FILE_FAILED);
        }
        return buffer.toString();
    }

    /**
     * 读取文件并返回文件内容
     *
     * @param inputStream
     * @return
     */
    public static String readFile(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try (InputStreamReader reader= new InputStreamReader(inputStream, StandardCharsets.UTF_8);BufferedReader bfReader = new BufferedReader(reader)){
           String tmpContent;
            while ((tmpContent = bfReader.readLine()) != null) {
                builder.append(tmpContent);
            }
        } catch (Exception e) {
            log.info(READ_FILE_FAILED, e);
            throw new CommonRuntimeException(READ_FILE_FAILED);
        }
        return builder.toString();
    }

    public static File createFile(String filePath) {
        try {
            File f = new File(filePath);
            if (f.exists()) {
                return f;
            }
            f.getParentFile().mkdirs();
            if (!f.createNewFile()) {
                throw new LjBaseRuntimeException(-1, "创建文件失败");
            }
            if (f.isDirectory()) {
                throw new LjBaseRuntimeException(-1,"filePath is dir:" + filePath);
            }
            return f;
        } catch (Exception e) {
            log.error("createFile error", e);
            return null;
        }
    }

    public static File createDir(String path) {
        try {
            File f = new File(path);
            f.getParentFile().mkdirs();
            f.mkdir();
            if (!f.isDirectory()) {
                throw new LjBaseRuntimeException(-1,"path is not dir:" + path);
            }
            return f;
        } catch (Exception e) {
            log.error("createFile error", e);
            return null;
        }
    }

    public static boolean addLine(File file, String msg) {
        try (FileOutputStream os = new FileOutputStream(file, true)) {
            os.write(msg.getBytes());
            os.write("\r\n".getBytes());
            os.flush();
            return true;
        } catch (Exception e) {
            log.error("write file error", e);
            return false;
        }

    }

    private static void transFiles(File srcFile,File dstFile){
        try (FileOutputStream out = new FileOutputStream(dstFile); FileInputStream in = new FileInputStream(srcFile)) {
            byte[] buffer = new byte[1024];
            int l;
            while ((l = in.read(buffer)) != -1) {
                out.write(buffer, 0, l);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static boolean copyFile(String src, String dst) {
        try {
            File srcFile = new File(src);
            if (!srcFile.exists()) {
                log.error("copyFile not exists:" + src);
                return false;
            }
            File dstFile = new File(dst);
            if (dstFile.isDirectory()) {
                log.error("dstFile is dir:" + dst);
                return false;
            }
            if (!dstFile.exists()) {
                dstFile.getParentFile().mkdirs();
                if (!dstFile.createNewFile()) {
                    throw new LjBaseRuntimeException(-1, "创建文件失败");
                }
            }
            transFiles(srcFile,dstFile);
            return true;
        } catch (Exception e) {
            log.error("cpoy file error", e);
            return false;
        }
    }

    public static void deleteFile(File rootFile) {
        if (!rootFile.exists()) {
            return;
        }
        if (!rootFile.isDirectory()) {
            try {
                Files.delete(Paths.get(rootFile.toURI()));
            } catch (IOException e) {
                throw new LjBaseRuntimeException(-1, "删除文件失败");
            }
            return;
        }
        File[] subFiles = rootFile.listFiles();
        for (File subFile : subFiles) {
            deleteFile(subFile);
        }
        try {
            Files.delete(Paths.get(rootFile.toURI()));
        } catch (IOException e) {
            throw new LjBaseRuntimeException(-1, "删除文件失败");
        }

    }

    private static void zip(File inputFileName, String zipFileName) throws Exception {
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            zip(out, inputFileName, "");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            base = (base.length() == 0 ? "" : base + "/");
            for (int i = 0; i < files.length; i++) {
                zip(out, files[i], base + files[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
           try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f))) {
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }
    }


    //压缩文件，inputFileName表示要压缩的文件（可以为目录）,zipFileName表示压缩后的zip文件
    public static void zip(String inputFileName, String zipFileName) throws Exception {
        File f = new File(zipFileName);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
        }
        zip(new File(inputFileName), zipFileName);
    }



    /**
     * 根据相对路径获取文件在项目下的绝对路径
     *
     * @param relativePath 相对路径
     * @return
     */
    public static String completedFilePath(String relativePath) {
        String[] dirs = relativePath.split("/");
        StringBuilder path = new StringBuilder();
        try {
            path.append(new File("").getCanonicalPath()).append(File.separator);
            for (String dir : dirs) {
                if (StringUtils.isEmpty(dir)) {
                    continue;
                }
                path.append(dir);
                if (!dir.contains(".")) {
                    path.append(File.separator);
                }
            }
        } catch (IOException e) {
            log.info("获取项目路径失败", e);
        }
        return path.toString();
    }

    /**
     * @return current execute directory
     */
    public static String execDir() {
        return System.getProperty("user.dir");
    }

    /**
     * 从inputStream中读取字节数组
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inputStream) {
         byte[] buffer = new byte[1024];
         byte[] result=null;
        int len;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            result=outputStream.toByteArray();
        } catch (IOException e) {
            log.info("读取inputStream异常", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.info("关闭输入流异常", e);
            }
        }
        return result;
    }

    /**
     * 通用下载文件
     *
     * @param filePath
     * @param response
     */
    public static void load(String filePath, HttpServletResponse response) {
        byte[] buff = new byte[1024];
        try (BufferedInputStream bis =new BufferedInputStream(new FileInputStream(filePath));OutputStream os =response.getOutputStream()){
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
