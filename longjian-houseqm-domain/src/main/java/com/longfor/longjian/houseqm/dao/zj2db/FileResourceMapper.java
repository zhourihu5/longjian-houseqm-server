package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.FileResource;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface FileResourceMapper extends LFMySQLMapper<FileResource> {


    List<FileResource> selectFileResourceByFileMd5InAndNoDeleted(@Param("attachments") List<String> attachments, @Param("deleted") String deleted);

    List<FileResource> searchByMd5In(@Param("attachmentsList") ArrayList<String> attachmentMd5List);
}