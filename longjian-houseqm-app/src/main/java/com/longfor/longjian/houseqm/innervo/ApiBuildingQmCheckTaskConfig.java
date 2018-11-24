package com.longfor.longjian.houseqm.innervo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lipeishuai
 * @date 2018/11/23 13:01
 */

@Data
@NoArgsConstructor
public class ApiBuildingQmCheckTaskConfig {

    /**
     * desc='整改负责人退单权限：10=能退单 20=不能退单')
     */
    private Integer repairer_refund_permission   = 0;
    /**
     * desc='整改参与人权限：10=只能提交描述 20=可以完成整改')
     */
    private Integer  repairer_follower_permission = 0;
    /**
     * desc='具有销项权的人能否销项由自己整改的问题：10=能销项 20=不能销项')
     */
    private Integer  checker_approve_permission=0;
    /**
     * desc='整改人完成整改时：10=不强制附加照片 20=必须附加整改照片'
     */
    private Integer repaired_picture_status=0;
    /**
     *  '创建问题缺陷时：10=文字描述和缺陷图片任意必填一项 20=必填文字描述 30=必填缺陷图片 40=文字描述和缺陷图片都必填 50=文字描述和缺陷图片都不填')
     */
    private Integer issue_desc_status=0;
    /**
     * desc='创建问题时默认描述(issue_desc_status=50时生效)')
     */
    private String issue_default_desc="";

}
