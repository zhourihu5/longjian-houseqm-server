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
public class OrgListVo implements Serializable{

    private List<OrgVo> result;

    @Data
    @NoArgsConstructor
    public class OrgVo implements Serializable {

        private int id;
        private String name;
        private List<ProjVo> projs;
    }

    @Data
    @NoArgsConstructor
    public class ProjVo implements Serializable {
        private int id;
        private String name;
    }

}
