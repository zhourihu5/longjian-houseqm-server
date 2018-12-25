package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.vo.MyIssueAttachListVo;
import com.longfor.longjian.houseqm.app.vo.MyIssueListVo;
import com.longfor.longjian.houseqm.app.vo.MyIssueMemberListVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;

import javax.servlet.http.HttpServletRequest;

public interface IHouseqmService {
    /**
     *获取问题人员列表
     * @param deviceReq
     * @return
     */
    TaskResponse<MyIssueListVo> myIssueLogList(DeviceReq deviceReq, HttpServletRequest request);

    /**
     * 获取指定任务与我相关的issue列表（增量接口
     * @param deviceReq
     * @param request
     * @return
     */
    TaskResponse<MyIssueListVo> myIssueList(DeviceReq deviceReq, HttpServletRequest request);

    /**
     * 读取与我相关的issue附件
     * @param deviceReq
     * @return
     */
    LjBaseResponse<MyIssueMemberListVo> issueMembers(DeviceReq deviceReq);

    /**
     * 读取指定任务与我相关的issue log列表（增量接口）
     * @param deviceReq
     * @return
     */
    LjBaseResponse<MyIssueAttachListVo> myIssueAttachementList(DeviceReq deviceReq);
}