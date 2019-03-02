package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.FileResourceMapper;
import com.longfor.longjian.houseqm.domain.internalservice.FileResourceService;
import com.longfor.longjian.houseqm.po.zj2db.FileResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 17:44
 */
@Service
@Slf4j
public class FileResourceServiceImpl implements FileResourceService {

    @Resource
    FileResourceMapper fileResourceMapper;

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param attachments
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.FileResource>
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<FileResource> searchFileResourceByFileMd5InAndNoDeleted(List<String> attachments) {
        return fileResourceMapper.selectFileResourceByFileMd5InAndNoDeleted(attachments,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<FileResource> searchByMd5In(ArrayList<String> attachmentMd5List) {
        return fileResourceMapper.searchByMd5In(attachmentMd5List);
    }
}
