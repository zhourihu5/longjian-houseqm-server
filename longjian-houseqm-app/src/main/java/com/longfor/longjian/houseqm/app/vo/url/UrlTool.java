package com.longfor.longjian.houseqm.app.vo.url;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.url
 * @ClassName: UrlTool
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 20:43
 */
@Data
@NoArgsConstructor
public class UrlTool implements Serializable {

    private Url url;
    private Map<String, List<String>> values;


}
