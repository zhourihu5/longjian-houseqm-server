package com.longfor.longjian.houseqm.graphql.fetcher;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.app.vo.StatDataVo;
import com.longfor.longjian.houseqm.app.vo.StatItemsVo;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/11/29 14:48
 */

@Slf4j
@Service
public class StatGroupDataFetcher{

    /**
     * itemsFetcher
     */
    public DataFetcher<StatItemsVo> progressStatDataFetcher = environment -> {

        StatItemsVo statItemsVo =new StatItemsVo();

        return statItemsVo;
    };

    /**
     * itemsFetcher
     */
    public DataFetcher<List<StatDataVo>> statGroupItemDataFetcher = environment -> {


        List<StatDataVo>  statDataVos = Lists.newArrayList();

        String categoryKey = environment.getArgument("categoryKey");

        log.debug("StatGroupDataFetcher - progressStatDataFetcher - categoryKey:{}", categoryKey);

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
