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
public class BoardListVo implements Serializable {

    private List<BoardVo> result;

    @Data
    @NoArgsConstructor
    public class BoardVo implements Serializable {

        private String label;
        private Integer value;
        private String name;
        private int key;
    }
}
