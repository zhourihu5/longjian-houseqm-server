package com.longfor.longjian.houseqm.app.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class MyIssueAttachListVo implements Serializable{

    private List<ApiHouseQmCheckTaskIssueAttachmentRspVo> attachment_list;
    private Integer last_id;


}
