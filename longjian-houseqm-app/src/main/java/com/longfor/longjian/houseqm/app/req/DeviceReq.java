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
    @NotNull
    private Integer last_id;//上次获取的最后ID，首次拉取传`0`
    @NotNull
    private Integer task_id;//任务ID
    @NotNull
    private Integer timestamp;//上次更新时间
    //private String token;

}
