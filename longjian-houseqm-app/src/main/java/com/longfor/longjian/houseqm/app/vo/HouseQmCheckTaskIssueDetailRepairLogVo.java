package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/1/12.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueDetailRepairLogVo implements Serializable {
    private Integer user_id;
    private String user_name;
    private Integer create_at;
    private String content;
    private String attachment_md5_list;

}
