package com.longfor.longjian.houseqm.domain.internalService.stat;

import com.github.pagehelper.util.StringUtil;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.common.time.TimeFrame;
import com.longfor.longjian.houseqm.dao.stat.StatHouseQmProjectDailyStatMapper;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectDailyStat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2019/1/17 17:15
 */

@Service
@Slf4j
public class StatHouseQmProjectDailyStatService {

    @Resource
    StatHouseQmProjectDailyStatMapper statHouseQmProjectDailyStatMapper;

    /**
     // stat_house_qm_project_daily_stat_20181116
     // stat_house_qm_project_weekly_stat_201846
     // stat_house_qm_project_monthly_stat_201811
     // stat_house_qm_project_yearly_stat_2018
     // stat_house_qm_project_quarterly_stat_20191
     *
     * @param timeFrame
     * @param categoryKey
     * @param projectIds
     * @return
     */
    @LFAssignDataSource("zhijian2_stat")
    public List<StatHouseQmProjectDailyStat> query(TimeFrame timeFrame,String categoryKey, List<Integer> projectIds){

        String table = "stat_house_qm_project_daily_stat" + timeFrame.getTableIdx();

        Condition condition = new Condition(StatHouseQmProjectDailyStat.class);
        /**
         * 因为统计表是分表的，故要使用动态表名
         * （1）StatHouseQmProjectDailyStat 必须实现 IDynamicTableName接口，默认的getDynamicTableName 方法返回null即可
         * （2）timeFrame的getTableIdx方法会返回相应的表的后缀，如天表_20181116、周表_201846等，后者表示2018年的第46周
         * （3）使用condition设置表名称：condition.setTableName;
         */
        condition.setTableName(table);
        Example.Criteria c = condition.createCriteria();

        if(StringUtil.isNotEmpty(categoryKey)){
            c.andEqualTo("categoryKey", categoryKey);
        }
        c.andIn("projectId", projectIds);

        List<StatHouseQmProjectDailyStat> stats =  statHouseQmProjectDailyStatMapper.selectByExample(condition);
        log.debug("StatHouseQmProjectDailyStat query:{}, result size:{}", condition.getDynamicTableName(), stats.size());

        return stats;

    }

}
