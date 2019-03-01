package com.longfor.longjian.houseqm.app.service.impl;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.ModuleInfoEnum;
import com.longfor.longjian.houseqm.app.service.ScanMsgPushService;
import com.longfor.longjian.houseqm.domain.internalService.StatScanRecordService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.StatScanRecord;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Wang on 2019/2/28.
 */
@Slf4j
@Service
public class ScanMsgPushServiceImpl implements ScanMsgPushService {

    @Resource
    private StatScanRecordService statScanRecordService;


    @Override
    public LjBaseResponse scanNoticeCenter(String category_cls) {

        Integer [] module_ids={};

        if(StringUtils.isNotBlank(category_cls)){
            String [] module=category_cls.split(",");
            for(int i=0;i<module.length;i++){
                module_ids[i]=Integer.parseInt(module[i]);
            }
        }else{
            module_ids[0]=ModuleInfoEnum.GCGL.getValue();
            module_ids[1]=ModuleInfoEnum.FHYS.getValue();
            module_ids[2]=ModuleInfoEnum.RHYF.getValue();
            module_ids[3]=ModuleInfoEnum.CJCY.getValue();
            module_ids[4]=ModuleInfoEnum.GDKF.getValue();
        }
        log.info("notice center begin to scan, task list:[%s]", StringUtils.join(module_ids, ','));


        for(Integer moduleId:module_ids){
            scan_notice_center_and_push(moduleId);
        }


        return null;
    }

    private void scan_notice_center_and_push(Integer moduleId){


        long stat_timestamp=60 * 60 * 24;

        long now=new Date().getTime();

        Date stat_beg= DateUtil.timeStampToDate(new Long(now -stat_timestamp).intValue(),"yy-mm-dd HH:mm:ss");
        Date stat_end=DateUtil.timeStampToDate(new Long(now).intValue(),"yy-mm-dd HH:mm:ss");


        List<StatScanRecord> statScanRecordList=statScanRecordService.findByExample(moduleId);







    }

}
