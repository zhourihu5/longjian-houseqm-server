package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.base.LjBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Wang on 2019/2/28.
 */
@RestController
@RequestMapping("scanning/v1/fb/scan/")
@Slf4j
public class ScanMsgPushController {
    @PostMapping(value = "scan_notice_center", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse scanNoticeCenter(String category_cls){

        log.info("scan_notice_center, category_cls=%s" + category_cls);


        return new LjBaseResponse();
    }


}
