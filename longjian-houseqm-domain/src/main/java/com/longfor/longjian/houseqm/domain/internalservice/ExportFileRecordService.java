package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;

import java.util.Date;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalservice
 * @ClassName: ExportFileRecordService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 11:37
 */
public interface ExportFileRecordService {

    ExportFileRecord insertFull(int userId, Integer teamId, Integer projectId, int exportType, String params, String resultFilePath, String resultName, int status, String errorMsg, Date executeAt);
}
