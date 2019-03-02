package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Dongshun on 2019/1/24.
 */
@Data
@NoArgsConstructor
public class IssueMapBody implements Serializable {
    private Map exist_issue_map;
    private Map notify_stat_map;
    private List delete_issue_uuids;
}
