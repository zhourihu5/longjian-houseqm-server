package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class DeviceReq implements Serializable {

    //private Integer task_ids;
    //private Long device_id;
    private Integer last_id;
    @NotNull
    private Integer task_id;//任务ID
    private Integer timestamp = 0;//上次更新时间
    //private String token;

}
