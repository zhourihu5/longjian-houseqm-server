package com.longfor.longjian.houseqm.app.vo;

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
public class CheckerStatListVo implements Serializable {

    private List<CheckerStatVo> items;

    @Data
    @NoArgsConstructor
    public class CheckerStatVo implements Serializable {

        private Integer checked_count;
        private Integer issue_count;
        private String real_name;
        private Integer records_count;
        private Integer user_id;
    }
}
