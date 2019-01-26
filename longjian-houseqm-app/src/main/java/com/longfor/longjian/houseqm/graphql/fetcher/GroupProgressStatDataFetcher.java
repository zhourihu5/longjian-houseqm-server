package com.longfor.longjian.houseqm.graphql.fetcher;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.time.TimeType;
import com.longfor.longjian.houseqm.app.vo.StatDataVo;
import com.longfor.longjian.houseqm.app.vo.StatItemsVo;
import com.longfor.longjian.houseqm.domain.internalService.stat.StatHouseQmProjectDailyStatService;
import com.longfor.longjian.houseqm.graphql.data.PassedVariableVo;
import com.longfor.longjian.houseqm.graphql.data.TimeFrameTypeEnum;
import graphql.schema.DataFetcher;
import graphql.schema.idl.EnumValuesProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 *  DataFetcher类是获取数据的服务
 *
 * @author lipeishuai
 * @date 2018/11/29 14:48
 */

@Slf4j
@Service
public class GroupProgressStatDataFetcher {

    @Resource
    StatHouseQmProjectDailyStatService statHouseQmProjectDailyStatService;

    public static EnumValuesProvider timeFrameTypeResolver = TimeFrameTypeEnum::valueOf;


    /**
     *  将schema的progressStat方法中参数值缓存到environment的source中
     * itemsFetcher
     */
    public  DataFetcher<StatItemsVo> progressStatDataFetcher = environment -> {

        /*
        envionment中的参数只能是progressStat中的， 在statGroupItemDataFetcher中就没有了
         */
        String categoryKey = environment.getArgument("categoryKey");
        String timeFrameType = environment.getArgument("timeFrameType");
        List<Integer> teamIds = environment.getArgument("teamIds");
        Date timeFrameBegin = environment.getArgument("timeFrameBegin");
        Date timeFrameEnd = environment.getArgument("timeFrameEnd");

        log.debug("StatGroupDataFetcher - progressStatDataFetcher - categoryKey:{}", categoryKey);
        log.debug("StatGroupDataFetcher - progressStatDataFetcher - timeFrameType:{}", timeFrameType);
        log.debug("StatGroupDataFetcher - progressStatDataFetcher - teamIds size:{}", teamIds.size());
        if(CollectionUtils.isNotEmpty(teamIds)){
            for(Integer teamId: teamIds){
                log.debug("StatGroupDataFetcher - progressStatDataFetcher - teamIds-teamId:{}", teamId);
            }
        }
        log.debug("StatGroupDataFetcher - progressStatDataFetcher - timeFrameBegin:{}", timeFrameBegin);
        log.debug("StatGroupDataFetcher - progressStatDataFetcher - timeFrameEnd:{}", timeFrameEnd);


        StatItemsVo statItemsVo =new StatItemsVo();

        PassedVariableVo vo=new PassedVariableVo();
        vo.setCategoryKey(categoryKey);
        vo.setTimeFrameType(timeFrameType);
        vo.setTeamIds(teamIds);
        vo.setBeginDate(timeFrameBegin);
        vo.setEndDate(timeFrameEnd);
        statItemsVo.setVariableVo(vo);
        return statItemsVo;
    };

    /**
     *  对应zhijian_server_stat_houseqm\app\graphqls\ats\group_stat\stat_group_progress_ast.py的方法resolve_progress_stat()
     *
     * itemsFetcher
     */
    public  DataFetcher<List<StatDataVo>> statGroupItemDataFetcher = environment -> {


        List<StatDataVo>  statDataVos = Lists.newArrayList();

        // T getSource()是父Field的对象
        StatItemsVo statItemsVo = environment.getSource();

        String categoryKey =  statItemsVo.getVariableVo().getCategoryKey();
        String timeFrameType = statItemsVo.getVariableVo().getTimeFrameType();
        List<Integer> teamIds = statItemsVo.getVariableVo().getTeamIds();
        Date timeFrameBegin = statItemsVo.getVariableVo().getBeginDate();
        Date timeFrameEnd = statItemsVo.getVariableVo().getEndDate();
        Integer timeFrameMaxCount =  statItemsVo.getVariableVo().getTimeFrameMax();


        log.debug("StatGroupDataFetcher - statGroupItemDataFetcher - categoryKey:{}", categoryKey);

        if(TimeType.WEEK.toString().equalsIgnoreCase(timeFrameType)){

            statHouseQmProjectDailyStatService.searchStat(categoryKey,TimeType.WEEK.getValue(),teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);

        }else if(TimeType.QUARTER.toString().equalsIgnoreCase(timeFrameType)){

            statHouseQmProjectDailyStatService.searchStat(categoryKey,TimeType.QUARTER.getValue(),teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);

        }else if(TimeType.MONTH.toString().equalsIgnoreCase(timeFrameType)){

            statHouseQmProjectDailyStatService.searchStat(categoryKey,TimeType.MONTH.getValue(),teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);

        }else if(TimeType.YEAR.toString().equalsIgnoreCase(timeFrameType)){

            statHouseQmProjectDailyStatService.searchStat(categoryKey,TimeType.YEAR.getValue(),teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);
        }else{
            //day
            statHouseQmProjectDailyStatService.searchStat(categoryKey,TimeType.DAY.getValue(),teamIds,
                    timeFrameBegin, timeFrameEnd, timeFrameMaxCount);
        }



        StatDataVo statDataVo = new StatDataVo();
        statDataVo.setIssueCount(100);
        statDataVo.setTotalAcreage(8777);
        statDataVo.setBeginOn("201809");
        statDataVo.setEndOn("201810");
        statDataVo.setYear(2018);
        statDataVo.setTimeFrameType("Month");


        StatDataVo statDataVo1 = new StatDataVo();
        statDataVo1.setIssueCount(99);
        statDataVo1.setTotalAcreage(1227);
        statDataVo1.setBeginOn("201807");
        statDataVo1.setEndOn("201811");
        statDataVo1.setYear(2018);
        statDataVo1.setTimeFrameType("Month");


        statDataVos.add(statDataVo);
        statDataVos.add(statDataVo1);

        return statDataVos;


    };

}
