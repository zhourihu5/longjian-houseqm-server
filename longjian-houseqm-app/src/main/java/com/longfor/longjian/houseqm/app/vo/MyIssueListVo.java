package com.longfor.longjian.houseqm.app.vo;

import com.longfor.longjian.houseqm.app.vo.houseqm.ApiHouseQmCheckTaskIssueRsp;
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
public class MyIssueListVo implements Serializable {

    private List<ApiHouseQmCheckTaskIssueRsp> issue_list;
    private Integer last_id;


}
