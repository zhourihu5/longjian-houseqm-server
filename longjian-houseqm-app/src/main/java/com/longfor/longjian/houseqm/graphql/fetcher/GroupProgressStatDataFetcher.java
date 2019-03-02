package com.longfor.longjian.houseqm.graphql.fetcher;

import com.longfor.longjian.common.time.TimeType;
import com.longfor.longjian.houseqm.app.service.StatHouseQmProjectStatService;
import com.longfor.longjian.houseqm.app.vo.StatDataVo;
import com.longfor.longjian.houseqm.app.vo.StatItemsVo;
import com.longfor.longjian.houseqm.graphql.data.PassedVariableVo;
import com.longfor.longjian.houseqm.graphql.data.TimeFrameTypeEnum;
import graphql.schema.DataFetcher;
import graphql.schema.idl.EnumValuesProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * DataFetcher类是获取数据的服务
 *
 * @author lipeishuai
 * @date 2018/11/29 14:48
 */

@Slf4j
@Service
public class GroupProgressStatDataFetcher {

    public static EnumValuesProvider timeFrameTypeResolver = TimeFrameTypeEnum::valueOf;
    /**
     * 将schema的progressStat方法中参数值缓存到environment的source中
     * itemsFetcher
     */
    public DataFetcher<StatItemsVo> progressStatDataFetcher = environment -> {

        /*
        envionment中的参数只能是progressStat中的， 在statGroupItemDataFetcher中就没有了
         */
        String categoryKey = environment.getArgument("categoryKey");
        String timeFrameType = environment.getArgument("timeFrameType");
        List<Integer> teamIds = environment.getArgument("teamIds");
        Date timeFrameBegin = environment.getArgument("timeFrameBegin");
        Date timeFrameEnd = environment.getArgument("timeFrameEnd");
        Integer timeFrameMax = environment.getArgument("timeFrameMax");

        log.debug("StatGroupDataFetcher - progressStatDataFetcher - categoryKey:{}，timeFrameType:{}，" +
                        "timeFrameBegin:{}, timeFrameEnd:{},timeFrameMax:{} ",
                categoryKey, timeFrameType, timeFrameBegin, timeFrameEnd, timeFrameMax);

        StatItemsVo statItemsVo = new StatItemsVo();

        PassedVariableVo vo = new PassedVariableVo();
        vo.setCategoryKey(categoryKey);
        vo.setTimeFrameType(timeFrameType);
        vo.setTeamIds(teamIds);
        vo.setBeginDate(timeFrameBegin);
        vo.setEndDate(timeFrameEnd);
        vo.setTimeFrameMax(timeFrameMax);
        statItemsVo.setVariableVo(vo);
        return statItemsVo;
    };
    @Resource
    StatHouseQmProjectStatService statHouseQmProjectStatService;
    /**
     * 对应zhijian_server_stat_houseqm\app\graphqls\ats\group_stat\stat_group_progress_ast.py的方法resolve_progress_stat()
     * <p>
     * itemsFetcher
     */
    public DataFetcher<List<StatDataVo>> statGroupItemDataFetcher = environment -> {

        // T getSource()是父Field的对象
        StatItemsVo statItemsVo = environment.getSource();

        String categoryKey = statItemsVo.getVariableVo().getCategoryKey();
        String timeFrameType = statItemsVo.getVariableVo().getTimeFrameType();
        List<Integer> teamIds = statItemsVo.getVariableVo().getTeamIds();
        Date timeFrameBegin = statItemsVo.getVariableVo().getBeginDate();
        Date timeFrameEnd = statItemsVo.getVariableVo().getEndDate();
        Integer timeFrameMaxCount = statItemsVo.getVariableVo().getTimeFrameMax();
        Integer groupId = environment.getContext();

        log.debug("StatGroupDataFetcher - statGroupItemDataFetcher - groupId:{}", groupId);

        if (TimeType.WEEK.toString().equalsIgnoreCase(timeFrameType)) {

            return statHouseQmProjectStatService.searchStat(groupId, categoryKey, TimeType.WEEK.getValue(), teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);

        } else if (TimeType.QUARTER.toString().equalsIgnoreCase(timeFrameType)) {

            return statHouseQmProjectStatService.searchStat(groupId, categoryKey, TimeType.QUARTER.getValue(), teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);

        } else if (TimeType.MONTH.toString().equalsIgnoreCase(timeFrameType)) {

            return statHouseQmProjectStatService.searchStat(groupId, categoryKey, TimeType.MONTH.getValue(), teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);

        } else if (TimeType.YEAR.toString().equalsIgnoreCase(timeFrameType)) {

            return statHouseQmProjectStatService.searchStat(groupId, categoryKey, TimeType.YEAR.getValue(), teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);
        } else {
            //day
            return statHouseQmProjectStatService.searchStat(groupId, categoryKey, TimeType.DAY.getValue(), teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);
        }

    };

}
