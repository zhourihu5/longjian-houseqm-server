package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.common.exception.CommonRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
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

    /**
     * 读取文件并返回文件内容
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        StringBuilder buffer = new StringBuilder();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = new FileInputStream(filePath);
            // 每行的数据
            String line;
            reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\n");
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            log.info("文件不存在, filePath=", filePath, e);
            throw new CommonRuntimeException("文件不存在, filePath=" + filePath);
        } catch (IOException e) {
            log.info("读取文件失败", e);
            throw new CommonRuntimeException("读取文件失败");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.info("reader关闭失败", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.info("is关闭失败", e);
                }
            }
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
        InputStreamReader reader;
        BufferedReader bfReader = null;
        try {
            reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            bfReader = new BufferedReader(reader);
            String tmpContent;
            while ((tmpContent = bfReader.readLine()) != null) {
                builder.append(tmpContent);
            }
        } catch (Exception e) {
            log.info("读取文件失败", e);
            throw new CommonRuntimeException("读取文件失败");
        } finally {
            try {
                if (bfReader != null) {
                    bfReader.close();
                }
            } catch (IOException e) {
                log.info("bfReader关闭失败", e);
            }
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
            f.createNewFile();
            if (f.isDirectory()) {
                throw new RuntimeException("filePath is dir:" + filePath);
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
                throw new RuntimeException("path is not dir:" + path);
            }
            return f;
        } catch (Exception e) {
            log.error("createFile error", e);
            return null;
        }
    }

    public static boolean addLine(File file, String msg) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file, true);
            os.write(msg.getBytes());
            os.write("\r\n".getBytes());
            os.flush();
            os.close();
            return true;
        } catch (Exception e) {
            log.error("write file error", e);
            return false;
        } finally {
            if (os != null) {
                os = null;
            }
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
                dstFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(dstFile);
            FileInputStream in = new FileInputStream(srcFile);
            byte[] buffer = new byte[1024];
            int L = 0;
            while ((L = in.read(buffer)) != -1) {
                out.write(buffer, 0, L);
            }
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                in.close();
            }
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
            rootFile.delete();
            return;
        }
        File[] subFiles = rootFile.listFiles();
        for (File subFile : subFiles) {
            deleteFile(subFile);
        }
        rootFile.delete();

    }

    private static void zip(File inputFileName, String zipFileName) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFileName, "");
        out.close();
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
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
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

    public static void main(String[] args) throws Exception {
        zip("c://222", "c://666/1.zip");
        //deleteFile(new File("c://222"));
        //copyFile("C://flower.jpg", "c://222/222/222/1.jpg");
        //createDir("c://222/222/222/1");
    }

    /**
     * 根据相对路径获取文件在项目下的绝对路径
     *
     * @param relativePath 相对路径
     * @return
     */
    public static String completedFilePath(String relativePath) {
        String[] dirs = relativePath.split("/");
        StringBuffer path = null;
        try {
            path = new StringBuffer(new File("").getCanonicalPath()).append(File.separator);
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
        //todo
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.info("读取inputStream异常", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.info("关闭输出流异常", e);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                log.info("关闭输入流异常", e);
            }
        }
        return outputStream.toByteArray();
    }

    /**
     * 通用下载文件
     *
     * @param filePath
     * @param response
     */
    public static void Load(String filePath, HttpServletResponse response) {
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(filePath));
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
