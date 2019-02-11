package com.longfor.longjian.houseqm.app.controller.v3api;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmService;
import com.longfor.longjian.houseqm.app.vo.MyIssueAttachListVo;
import com.longfor.longjian.houseqm.app.vo.MyIssueListVo;
import com.longfor.longjian.houseqm.app.vo.MyIssueMemberListVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import com.longfor.longjian.houseqm.app.vo.houseqm.HouseqmMyIssueLogListRspVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * http://192.168.37.159:3000/project/8/interface/api/344
 * http://192.168.37.159:3000/project/8/interface/api/348
 * http://192.168.37.159:3000/project/8/interface/api/352
 * http://192.168.37.159:3000/project/8/interface/api/356
 * <p>
 * http://192.168.37.159:3000/project/8/interface/api/672 获取问题人员列表
 * http://192.168.37.159:3000/project/8/interface/api/674 获取指定任务与我相关的issue列表（增量接口
 * http://192.168.37.159:3000/project/8/interface/api/676  读取与我相关的issue附件
 * http://192.168.37.159:3000/project/8/interface/api/682 读取指定任务与我相关的issue log列表（增量接口）
 *
 * @author lipeishuai
 * @date 2018/11/20 18:01
 */
@RestController
@RequestMapping("/v3/api/houseqm/")
@Slf4j
public class HouseqmController {
    @Autowired
    private IHouseqmService houseqmService;
    /**
     * @param deviceReq
     * @return
     */
    @RequestMapping(value = "my_issue_log_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmMyIssueLogListRspVo> myIssueLogList(DeviceReq deviceReq, HttpServletRequest request) {
        return houseqmService.myIssueLogList(deviceReq,request);
    }


    /**
     * @param deviceReq
     * @return
     */
    @RequestMapping(value = "my_issue_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<MyIssueListVo> myIssueList(DeviceReq deviceReq,HttpServletRequest request) {
        return houseqmService.myIssueList(deviceReq,request);
    }


    /**
     * @param deviceReq
     * @return
     */
    @RequestMapping(value = "issue_members", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<MyIssueMemberListVo> issueMembers(DeviceReq deviceReq) {
        return houseqmService.issueMembers(deviceReq);
    }


    /**
     * @param deviceReq
     * @return
     */
    @RequestMapping(value = "my_issue_attachment_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<MyIssueAttachListVo> myIssueAttachmentList(DeviceReq deviceReq,HttpServletRequest request) {
        return houseqmService.myIssueAttachementList(deviceReq,request);
    }
}
