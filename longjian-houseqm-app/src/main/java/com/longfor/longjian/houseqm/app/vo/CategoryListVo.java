package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lipeishuai on 2018/11/11.
 */
@Data
@NoArgsConstructor
public class CategoryListVo implements Serializable{

    private List<CategoryVo> result;

    @Data
    @NoArgsConstructor
    public class CategoryVo implements Serializable {

        private String label;
        private String value;
        private String key;
        private String name;
        private String fatherKey;
        private boolean isLeaf;
    }

}
