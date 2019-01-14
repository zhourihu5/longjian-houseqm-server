package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.ExportFileRecordMapper;
import com.longfor.longjian.houseqm.domain.internalService.ExportFileRecordService;
import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService.impl
 * @ClassName: ExportFileRecordServiceImpl
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 11:37
 */
@Service
@Slf4j
public class ExportFileRecordServiceImpl implements ExportFileRecordService {


    @Resource
    private ExportFileRecordMapper exportFileRecordMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public ExportFileRecord insertFull(int userId, Integer teamId, Integer project_id, int exportType, String params, String resultFilePath, String resultName, int status, String errorMsg, Date executeAt) {
        ExportFileRecord item = new ExportFileRecord();
        item.setUserId(userId);
        item.setTeamId(teamId);
        item.setProjectId(project_id);
        item.setExportType(exportType);
        item.setParams(params);
        item.setResultFilePath(resultFilePath);
        item.setResultName(resultName);
        item.setStatus(status);
        item.setExecuteAt(executeAt);
        item.setCreateAt(new Date());
        item.setUpdateAt(new Date());
        item.setErrorMsg(errorMsg);

        exportFileRecordMapper.insertSelective(item);

        return item;
    }
}
