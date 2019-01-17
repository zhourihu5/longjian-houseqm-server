package com.longfor.longjian.houseqm.graphql.fetcher;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.app.vo.StatDataVo;
import com.longfor.longjian.houseqm.app.vo.StatItemsVo;
import com.longfor.longjian.houseqm.graphql.data.PassedVariableVo;
import com.longfor.longjian.houseqm.graphql.data.TimeFrameTypeEnum;
import graphql.schema.DataFetcher;
import graphql.schema.idl.EnumValuesProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

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
public class StatGroupDataFetcher{


    public static EnumValuesProvider timeFrameTypeResolver = TimeFrameTypeEnum::valueOf;


    /**
     * itemsFetcher
     */
    public static DataFetcher<StatItemsVo> progressStatDataFetcher = environment -> {

        /*
        envionment中的参数只能是progressStat中的， 在statGroupItemDataFetcher中就没有了
         */
        String categoryKey = environment.getArgument("categoryKey");
        String timeFrameType = environment.getArgument("timeFrameType");
        List<Integer> teamIds = environment.getArgument("teamIds");
        Date dateField = environment.getArgument("timeFrameEnd");

        log.debug("StatGroupDataFetcher - progressStatDataFetcher - categoryKey:{}", categoryKey);
        log.debug("StatGroupDataFetcher - progressStatDataFetcher - timeFrameType:{}", timeFrameType);
        log.debug("StatGroupDataFetcher - progressStatDataFetcher - teamIds size:{}", teamIds.size());
        if(CollectionUtils.isNotEmpty(teamIds)){
            for(Integer teamId: teamIds){
                log.debug("StatGroupDataFetcher - progressStatDataFetcher - teamIds-teamId:{}", teamId);
            }
        }
        log.debug("StatGroupDataFetcher - progressStatDataFetcher - timeFrameEnd:{}", dateField);


        StatItemsVo statItemsVo =new StatItemsVo();

        PassedVariableVo vo=new PassedVariableVo();
        vo.setCategoryKey(categoryKey);
        vo.setTimeFrameType(timeFrameType);
        vo.setTeamIds(teamIds);
        vo.setDateField(dateField);

        statItemsVo.setVariableVo(vo);
        return statItemsVo;
    };

    /**
     * itemsFetcher
     */
    public static DataFetcher<List<StatDataVo>> statGroupItemDataFetcher = environment -> {


        List<StatDataVo>  statDataVos = Lists.newArrayList();

        StatItemsVo statItemsVo = environment.getSource();
        log.debug("StatGroupDataFetcher - statGroupItemDataFetcher - categoryKey:{}", statItemsVo.getVariableVo().getCategoryKey());

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
