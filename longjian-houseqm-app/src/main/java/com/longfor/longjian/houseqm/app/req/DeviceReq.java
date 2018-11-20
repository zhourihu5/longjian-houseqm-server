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
public class DeviceReq implements Serializable {

    private Integer task_ids;
    private Long device_id;
    private Integer last_id;
    private Integer task_id;
    private Integer timestamp;
    private String token;

}
