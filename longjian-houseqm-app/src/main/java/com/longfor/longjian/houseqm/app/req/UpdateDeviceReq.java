package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class UpdateDeviceReq implements Serializable {

    private Integer issue_log_update_time;
    private Integer issue_members_update_time;
    private Integer issue_update_time;
    private Integer task_members_update_time;
    private Integer task_update_time;

    private Long device_id;
    private String token;

}
