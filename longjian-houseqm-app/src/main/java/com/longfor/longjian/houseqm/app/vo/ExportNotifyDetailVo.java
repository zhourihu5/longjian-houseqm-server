package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/2/18.
 */
@NoArgsConstructor
@Data
public class ExportNotifyDetailVo implements Serializable {
    private Integer issue_id = 0;
    private String task_name = "";
    private String area_name = "";
    private String check_item_name = "";
    private String content = "";
    private List<String> attachment_path = Lists.newArrayList();
}
