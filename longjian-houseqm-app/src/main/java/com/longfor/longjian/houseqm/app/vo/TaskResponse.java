package com.longfor.longjian.houseqm.app.vo;

import com.longfor.longjian.common.base.LjBaseResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lipeishuai
 * @date 2018/11/17 15:14
 */

@Data
@NoArgsConstructor
public class TaskResponse<T> extends LjBaseResponse implements Serializable {

    private String message;
    private String msg;

}
