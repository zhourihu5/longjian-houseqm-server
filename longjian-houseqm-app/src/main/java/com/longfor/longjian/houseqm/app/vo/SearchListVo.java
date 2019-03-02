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
public class SearchListVo implements Serializable {

    private List<SearchVo> items;

    @Data
    @NoArgsConstructor
    public class SearchVo implements Serializable {

        private Integer id;
        private String name;
        private Integer team_id;
        private Integer status;
        private Integer update_at;
    }
}
