package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.service.ScanMsgPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Wang on 2019/2/28.
 */
@RestController
@RequestMapping("scanning/v1/fb/scan/")
@Slf4j
public class ScanMsgPushController {

    @Resource
    private ScanMsgPushService scanMsgPushService;


    @RequestMapping(value = "scan_notice_center", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse scanNoticeCenter(String categoryCls) {

        log.info("scan_notice_center, categoryCls=" + categoryCls);

        scanMsgPushService.scanNoticeCenter(categoryCls);

        return new LjBaseResponse();
    }


}
