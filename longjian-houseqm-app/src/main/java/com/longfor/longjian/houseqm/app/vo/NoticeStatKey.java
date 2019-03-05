package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

/**
 * Created by Wang on 2019/3/1.
 */
@Data
public class NoticeStatKey {

    private Integer projectId;

    private Integer userId;

    public NoticeStatKey(Integer projectId, Integer userId) {
        this.projectId = projectId;
        this.userId = userId;
    }
}
