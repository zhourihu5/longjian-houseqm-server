package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.req.StatGroupReq;
import com.longfor.longjian.houseqm.app.service.GraphqlExecuteService;
import com.longfor.longjian.houseqm.graphql.schema.GroupProgressStatSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * http://192.168.37.159:3000/project/8/interface/api/594  获取集团下项目统计信息
 * http://192.168.37.159:3000/project/8/interface/api/600 获取集团下所有公司统计信息
 * <p>
 * http://192.168.37.159:3000/project/8/interface/api/932  获取集团本月和上月指标
 * http://192.168.37.159:3000/project/8/interface/api/972  获取集团下近七天每天的数据
 * http://192.168.37.159:3000/project/8/interface/api/1004 获取集团下搜索时段区域的趋势对比信息
 * http://192.168.37.159:3000/project/8/interface/api/996  获取集团下项目排名
 * http://192.168.37.159:3000/project/8/interface/api/1012 获取检查项的统计数据
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("gapi/v3/stat_houseqm/stat/")
@Slf4j
public class StatGroupController {

    private final static String TEAMRANKSTAT_TIP = "teamRankStat";

    /**
     * 项目进度统计
     */
    private final static String PROGRESSSTAT_TIP = "progressStat";
    private final static String PROJECTSTAT_TIP = "projectStat";
    private final static String PROJECTRANKSTAT_TIP = "projectRankStat";
    private final static String CATEGORYSTAT_TIP = "categoryStat";



    @Resource
    private GraphqlExecuteService graphqlExecuteService;

    @Resource
    private GroupProgressStatSchema groupProgressStatSchema;

    /**
     * @param groupId
     * @param pageLevel
     * @param tip
     * @param statGroupReq
     * @return
     */
    @PostMapping(value = "group", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> group(@RequestParam(value = "token", required =false) String token,
                                        @RequestParam(value = "group_id") Integer groupId,
                                        @RequestParam(value = "page_level") String pageLevel,
                                        @RequestParam(value = "tip") String tip,
                                        @RequestBody StatGroupReq statGroupReq) {

        LjBaseResponse response = new LjBaseResponse();
        try {

            Object statListVo = null;
            switch(tip){
                case TEAMRANKSTAT_TIP:
                    break;
                case PROGRESSSTAT_TIP:
                    statListVo = graphqlExecuteService.execute(groupId, PROGRESSSTAT_TIP, statGroupReq.getQuery(),
                            statGroupReq.getVariables(), groupProgressStatSchema.buildSchema());
                    break;
                case PROJECTSTAT_TIP:
                    break;
                case PROJECTRANKSTAT_TIP:
                    break;
                case CATEGORYSTAT_TIP:
                    break;
                default:
                    break;
            }

            response.setData(statListVo);

        }catch (IOException e){
            log.error("StatGroupController#group IOException,{}", e);
        }
        catch (LjBaseRuntimeException ex) {

            response.setResult(ex.getErrorCode());
            response.setMessage(ex.getErrorMsg());
            log.error("StatGroupController#group error,{}", ex);
        }

        return response;
    }


}
