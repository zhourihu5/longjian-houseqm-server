package com.longfor.longjian.houseqm.app.vo.url;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.url
 * @ClassName: ExportVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 11:28
 */
@Component
@ConfigurationProperties(prefix = "export")
@Data
public class ExportVo implements Serializable {
    private String base_dir;
    private String base_uri;
}
