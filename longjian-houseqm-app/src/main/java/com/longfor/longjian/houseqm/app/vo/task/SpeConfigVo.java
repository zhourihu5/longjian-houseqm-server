package com.longfor.longjian.houseqm.app.vo.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.task
 * @ClassName: SpeConfigVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/24 15:53
 */
@Component
@ConfigurationProperties(prefix = "spe")
@Data
public class SpeConfigVo {

    private TeamGroup team_group_100194;//prod
    private TeamGroup team_group_100044;//dev
    private TeamGroup team_group_100137;//test

    @Data
    public static class TeamGroup {
        private String export_issue;
    }

}
