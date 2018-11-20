package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.GroupReq;
import com.longfor.longjian.houseqm.app.vo.StatListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *  http://192.168.37.159:3000/project/8/interface/api/594  获取集团下项目统计信息
 *  http://192.168.37.159:3000/project/8/interface/api/600 获取集团下所有公司统计信息
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("gapi/v3/stat_houseqm/stat/")
@Slf4j
public class StatHouseqmController {


    /**
     *
     * @param groupReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "group", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatListVo> group(GroupReq groupReq){
        return null;
    }



}
