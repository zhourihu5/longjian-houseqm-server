package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.houseqm.app.vo.BoardListVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/1028 获取集团合作伙伴
 * http://192.168.37.159:3000/project/8/interface/api/1044 获取集团下合作伙伴统计信息
 * http://192.168.37.159:3000/project/8/interface/api/1068 获取集团下合作伙伴分项详情特定检查项数据
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("gapi/v3/stat_houseqm/stat/")
@Slf4j
public class PartnerBoardController {


    /**
     * @param pageLevel
     * @param groupId
     * @param tip
     * @return
     */
    @MockOperation
    @GetMapping(value = "partner_board", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<BoardListVo> doAction(@RequestParam(value = "page_level") String pageLevel,
                                              @RequestParam(value = "group_id") String groupId,
                                              @RequestParam(value = "tip") String tip) {


        return null;
    }
}
