package com.longfor.longjian.houseqm.app.feginClient;

import com.longfor.gaia.gfs.web.feign.LFFeignClient;
import com.longfor.gaia.gfs.web.feign.config.LFFeignConfiguration;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.HouseqmCheckTaskIssueIndexJsonReqMsg;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.HouseqmCheckTaskIssueIndexJsonRspMsg;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.feginClient
 * @ClassName: IHouseqmCheckTaskIssueFeignService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 16:16
 */
@Service
@LFFeignClient(group = "longjian-basic-server", value = "houseqmcheckissue", configuration = LFFeignConfiguration.class)
public interface IHouseqmCheckTaskIssueFeignService {

    @RequestMapping(value = "index_json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    LjBaseResponse<HouseqmCheckTaskIssueIndexJsonRspMsg> indexJson(@RequestBody HouseqmCheckTaskIssueIndexJsonReqMsg req);

}
