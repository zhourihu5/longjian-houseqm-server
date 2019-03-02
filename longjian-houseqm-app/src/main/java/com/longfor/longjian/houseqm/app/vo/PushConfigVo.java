package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: PushConfigVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 14:25
 */
@Component
@ConfigurationProperties(prefix = "push_config")
@Data
public class PushConfigVo implements Serializable {
    private String enterprise_id;
    private AppInfo rhapp;
    private AppInfo ydyf;
    private AppInfo gcgl;
    private AppInfo gxgl;

    @Data
    public static class AppInfo {
        private String app_key_android;
        private String app_master_secret_android;
        private String app_key_ios;
        private String app_master_secret_ios;
        private String app_secret_xiao_mi;
        private String package_name_xiao_mi;
    }
}
