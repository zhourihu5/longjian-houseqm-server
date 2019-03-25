package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.FileResourceMapper;
import com.longfor.longjian.houseqm.domain.internalservice.FileResourceService;
import com.longfor.longjian.houseqm.po.zj2db.FileResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
    private FileResourceMapper fileResourceMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<FileResource> searchFileResourceByFileMd5InAndNoDeleted(List<String> attachments) {
        if (CollectionUtils.isEmpty(attachments))return Lists.newArrayList();
        return fileResourceMapper.selectFileResourceByFileMd5InAndNoDeleted(attachments, "false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<FileResource> searchByMd5In(ArrayList<String> attachmentMd5List) {
        if (CollectionUtils.isEmpty(attachmentMd5List))return Lists.newArrayList();
        return fileResourceMapper.searchByMd5In(attachmentMd5List);
    }
}
