package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.CategoryListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * http://192.168.37.159:3000/project/8/interface/api/18   获取集团下统计汇总工程检查统计分析检查项列表
 * http://192.168.37.159:3000/project/8/interface/api/1212 公司统计分析获取检查项标准
 * http://192.168.37.159:3000/project/8/interface/api/1260 公司统计分析组织对比项目对比/获取检查项
 * http://192.168.37.159:3000/project/8/interface/api/1292 公司统计分析组织对比总包对比获取检查项
 * http://192.168.37.159:3000/project/8/interface/api/1476 公司统计分析分项详情获取检查项
 * http://192.168.37.159:3000/project/8/interface/api/1532 项目下统计分析项目总览获取检查项标准
 * http://192.168.37.159:3000/project/8/interface/api/1564 项目统计分析整改进度获取检查项
 * http://192.168.37.159:3000/project/8/interface/api/1572 项目统计分析问题分析获取检查项
 * http://192.168.37.159:3000/project/8/interface/api/1596 项目统计分析区域对比获取检查项
 * http://192.168.37.159:3000/project/8/interface/api/1620 项目统计分析分项详情获取检查项列表
 * http://192.168.37.159:3000/project/8/interface/api/1644 项目统计分析人员对比获取检查项
 *
 * http://192.168.37.159:3000/project/8/interface/api/530  获取检查项标准
 *
 *
 *
 * @author lipeishuai
 * @date 2018/11/17 14:57
 */
@RestController
@RequestMapping("gapi/v3/res/")
@Slf4j
public class CategoryController {

    /**
     * 获取集团下统计汇总工程检查统计分析检查项列表
     *
     * @param groupId
     * @param pageLevel
     * @param tip
     * @return
     */
    @MockOperation
    @GetMapping(value = "category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CategoryListVo> doAction(@RequestParam(value="group_id", required = false, defaultValue = "1") Integer groupId,
                                                   @RequestParam(value="page_level",required = false, defaultValue = "group") String pageLevel,
                                                   @RequestParam(value="tip",required = false) String tip){


        return null;
    }


}
