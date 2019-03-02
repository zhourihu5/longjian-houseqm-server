package com.longfor.longjian.houseqm.app.vo.export;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Date 2019/2/19 11:33
 * Created by Jiazhongmin
 */
@Data
@NoArgsConstructor
public class NodeDataVo implements Serializable {
    private String key = "";
    private String parent_key = "";
    private Integer issue_count = 0;
    private String name = "";
    private String path_name = "";
    private List<String> path_keys = Lists.newArrayList();
    private Integer child_count = 0;
    private Boolean valid_node = true;
}
