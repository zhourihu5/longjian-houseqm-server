package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.houseqm.app.appService.CompanyService;
import com.longfor.longjian.houseqm.app.vo.CompanyListVo;
import com.longfor.longjian.houseqm.app.vo.CompanyVo;
import com.longfor.longjian.houseqm.app.vo.LevelVo;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.CompanyLevelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *  http://192.168.37.159:3000/mock/8/uc/company/company_list
 *
 * @author lipeishuai
 * @date 2018-11-11 18:34
 */
@RestController
@RequestMapping("uc/company/")
@Slf4j
public class CompanyController {

    public static String ZhijianCloud_ProdEnv = "29_prod";
    public static String  Zhongliang_ProdEnv = "33_zl";
    public static String  Test7_ProdEnv = "d07_test7";
    public static String Test4_ProdEnv = "04_test4";

    @Resource
    private CompanyService companyService;

    /**
     * 公司列表
     *
     * http://192.168.37.159:3000/mock/8/uc/company/company_list
     *
     * @param groupId
     * @param pageLevel
     * @return
     */
    @GetMapping(value = "company_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CompanyListVo> companyList(@RequestParam(value="group_id", required = false, defaultValue = "1") Integer groupId,
                                                                  @RequestParam(value="page_level",required = false, defaultValue = "group") String pageLevel) {

        LjBaseResponse<CompanyListVo> response = new LjBaseResponse<>();

        List<CompanyVo> vos =companyService.getAllCompany(groupId);

        CompanyListVo companyListVo = new CompanyListVo();
        companyListVo.setItems(vos);
        companyListVo.setLevels(getLevels());

        response.setData(companyListVo);
        //response.setExtraMessage(String.valueOf(CompanyLevelEnum.values()));
        log.info("{}", response);
        return response;
    }


    /**
     *  使用Mock先产生一个URL的输出结果
     *
     * @param requestParam
     * @return
     */
    @MockOperation
    @GetMapping(value = "mycompany", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CompanyListVo> myCompanyList(RequestParam requestParam){

        return null;
    }




    /**
     * 得到level字典
     * @return
     */
    private List<LevelVo> getLevels(){

        List<LevelVo> levelVoList = Lists.newArrayList();
        for(CompanyLevelEnum enm: CompanyLevelEnum.values()){
            LevelVo vo= new LevelVo();
            vo.setValue(enm.getId());
            vo.setName(enm.getValue());
            levelVoList.add(vo);
        }
        return levelVoList;
    }




}
