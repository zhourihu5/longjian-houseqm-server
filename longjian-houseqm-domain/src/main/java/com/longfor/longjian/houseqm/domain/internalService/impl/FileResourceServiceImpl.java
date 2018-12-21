package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.FileResourceMapper;
import com.longfor.longjian.houseqm.domain.internalService.FileResourceService;
import com.longfor.longjian.houseqm.po.FileResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     * @return java.util.List<com.longfor.longjian.houseqm.po.FileResource>
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<FileResource> searchFileResourceByFileMd5InAndNoDeleted(List<String> attachments) {
        return fileResourceMapper.selectFileResourceByFileMd5InAndNoDeleted(attachments,"false");
    }
}
