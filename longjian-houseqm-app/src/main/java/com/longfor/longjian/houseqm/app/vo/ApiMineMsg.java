package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/21.
 */
@Data
@NoArgsConstructor
public class ApiMineMsg implements Serializable {
    private List<ApiMineTeamsMsg> teams;
    private List<ApiMineProjectsMsg> projects;

    @Data
    @NoArgsConstructor
    public class ApiMineTeamsMsg implements Serializable {
        private Integer id = 0;
        private String team_name = "";
        private Integer parent_team_id = 0;
        private Integer update_at = 0;
        private Integer delete_at = 0;
    }

    @Data
    @NoArgsConstructor
    public class ApiMineProjectsMsg implements Serializable {
        private Integer id = 0;
        private String name = "";
        private Integer team_id = 0;
        private Integer update_at = 0;
        private Integer delete_at = 0;
    }

}
