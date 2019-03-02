package com.longfor.longjian.houseqm.app.feginclient;

import com.longfor.gaia.gfs.web.feign.LFFeignClient;
import com.longfor.gaia.gfs.web.feign.config.LFFeignConfiguration;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.req.feignClientReq.ProjectPermissionReq;
import com.longfor.longjian.common.req.feignClientReq.TeamPermissionReq;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.feginclient
 * @ClassName: IPermissionFeignService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/17 16:53
 */
@Service
@LFFeignClient(group = "longjian-basic-server", value = "permission", configuration = LFFeignConfiguration.class)
public interface IPermissionFeignService {

    /**
     * 项目鉴权
     * 检查用户是否有此项目权限
     *
     * @param projectPermissionReq
     * @return
     */
    @RequestMapping(value = "project_permission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    LjBaseResponse<Object> projectPermission(@Valid @RequestBody ProjectPermissionReq projectPermissionReq);

    /**
     * 公司鉴权
     * 检查用户是否有此集团/公司权限
     *
     * @param teamPermissionReq
     * @return
     */
    @RequestMapping(value = "team_permission", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    LjBaseResponse<Object> teamPermission(@Valid @RequestBody TeamPermissionReq teamPermissionReq);

}
