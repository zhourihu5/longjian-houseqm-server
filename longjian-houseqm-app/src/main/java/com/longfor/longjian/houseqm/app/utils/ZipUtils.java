package com.longfor.longjian.houseqm.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Created by Dongshun on 2019/2/19.
 */
public class ZipUtils {
    private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);

    public static void zip(String inputFileName, String zipFileName)
            throws Exception {
        zip(zipFileName, new File(inputFileName));
    }

    private static void zip(String zipFileName, File inputFile)
            throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        zip(out, inputFile, "");
        log.info("zip done");
        out.close();
    }

    private static void zip(ZipOutputStream out, File f, String base)
            throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            try (FileInputStream in = new FileInputStream(f)) {
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 创建ZIP文件
     *
     * @param sourcePath 文件或文件夹路径
     * @param zipPath    生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(String sourcePath, String zipPath) {
        try (FileOutputStream fos = new FileOutputStream(zipPath); ZipOutputStream zos = new ZipOutputStream(fos)) {
            writeZip(new File(sourcePath), "", zos);
        } catch (Exception e) {
            log.error("ZipUtils createZip  Failed to create ZIP file", e);
        } finally {
            //压缩成功后，删除打包前的文件
            deleteFile(new File(sourcePath));
        }
    }

    private static void writeZip(File file, String parentPath,
                                 ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {// 处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                for (File f : files) {
                    writeZip(f, parentPath, zos);
                }
            } else {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }
                } catch (Exception e) {
                    log.error("ZipUtils createZip  Failed to create ZIP file", e);
                }
            }
        }
    }

    private static void copyResource(List<String> oldResPath, String newResPath) {
        for (String s : oldResPath) {
            try {
                // 如果文件夹不存在 则建立新文件夹
                (new File(newResPath)).mkdirs();
                File a = new File(s);
                // 如果已经是具体文件，读取
                if (a.isFile()) {
                    readFile(newResPath, a);
                }
                // 如果文件夹下还存在文件，遍历，直到得到具体的文件
                else {
                    String[] file = a.list();
                    File temp ;
                    assert file != null;
                    for (String s1 : file) {
                        if (s.endsWith(File.separator)) {
                            temp = new File(s + s1);
                        } else {
                            temp = new File(s + File.separator + s1);
                        }

                        if (temp.isFile()) {
                            readFile(newResPath, temp);
                        }
                        if (temp.isDirectory()) {
                            List<String> oldChildPath = new ArrayList<>();
                            oldChildPath.add(s + "/" + s1);
                            newResPath = String.format("%s%s%s", newResPath, "/", s1);
                            // 如果是子文件夹 递归循环
                            copyResource(oldChildPath, newResPath);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("copy all files failed", e);
            }
        }
    }

    private static void readFile(String newResPath, File a) {
        try (FileInputStream input = new FileInputStream(a); FileOutputStream output = new FileOutputStream(newResPath + "/" + (a.getName()))) {
            byte[] b = new byte[1024 * 4];
            int len;
            while ((len = input.read(b)) != -1) {
                output.write(b, 0, len);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                boolean delete = file.delete();
                if (delete) {
                    log.info("删除成功");
                }
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            boolean delete = file.delete();
            if (delete) {
                log.info("删除成功");
            }
        }
    }

    /**
     * 时间格式化
     *
     * @return
     */
    public static String dateToString() {
        Date d = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(d);
    }


    //main方法测试
    public static void main(String[] args) {
        List<String> oldResPath = new ArrayList<>();
        // excel文件路径
        oldResPath.add("d:\\a.xlsx");
        oldResPath.add("d:\\b.xlsx");
        String newResPath = "d:\\" + "excel" + dateToString();
        String zipPath = newResPath + ".zip";

        copyResource(oldResPath, newResPath);
        createZip(newResPath, zipPath);
    }
}

