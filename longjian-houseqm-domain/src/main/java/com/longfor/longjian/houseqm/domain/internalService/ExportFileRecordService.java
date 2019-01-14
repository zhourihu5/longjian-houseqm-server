package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;

import java.util.Date;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService
 * @ClassName: ExportFileRecordService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 11:37
 */
public interface ExportFileRecordService {

    ExportFileRecord insertFull(int userId, Integer teamId, Integer project_id, int exportType, String params, String resultFilePath, String resultName, int status, String errorMsg, Date executeAt);
}
