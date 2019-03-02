package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.vo.MyIssueAttachListVo;
import com.longfor.longjian.houseqm.app.vo.MyIssueListVo;
import com.longfor.longjian.houseqm.app.vo.MyIssueMemberListVo;
import com.longfor.longjian.houseqm.app.vo.houseqm.HouseqmMyIssueLogListRspVo;
import com.longfor.longjian.houseqm.po.zj2db.Area;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IHouseqmService {


    List<Integer> searchHouseQmApproveUserIdInMyCheckSquad(int userId, int taskId);

    LjBaseResponse<HouseqmMyIssueLogListRspVo> myIssueLogList(DeviceReq deviceReq, HttpServletRequest request);

    LjBaseResponse<MyIssueListVo> myIssueList(DeviceReq deviceReq, HttpServletRequest request);


    LjBaseResponse<MyIssueMemberListVo> issueMembers(DeviceReq deviceReq);

    LjBaseResponse<MyIssueAttachListVo> myIssueAttachementList(DeviceReq deviceReq, HttpServletRequest request);

    List<Area> searchTargetAreaByTaskId(Integer projectId,Integer taskId);

}
