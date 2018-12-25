package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.FileResource;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface FileResourceMapper extends LFMySQLMapper<FileResource> {

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param attachments
     * @param deleted
     * @return java.util.List<com.longfor.longjian.houseqm.po.FileResource>
     */
    List<FileResource> selectFileResourceByFileMd5InAndNoDeleted(@Param("attachments") List<String> attachments, @Param("deleted") String deleted);

    List<FileResource> searchByMd5In(@Param("attachmentsList")ArrayList<String> attachmentMd5List);
}