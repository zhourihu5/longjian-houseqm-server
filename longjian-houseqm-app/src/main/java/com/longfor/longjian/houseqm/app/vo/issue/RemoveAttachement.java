package com.longfor.longjian.houseqm.app.vo.issue;

import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.service.ApiListing;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issue
 * @ClassName: RemoveAttachement
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 15:40
 */
@Data
@NoArgsConstructor
public class RemoveAttachement implements Serializable {
    private String issueUuid;
    private String md5;
}
