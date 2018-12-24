package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/22 0022 16:16
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticCategoryIssueListRspMsgVo implements Serializable {

    private Integer total;
    private List<ApiTaskIssueRepairListRsp> issueList;


    @Data
    @NoArgsConstructor
    public class ApiTaskIssueRepairListRsp implements Serializable{
        private Integer id;
        private Integer projectId;
        private Integer taskId;
        private String uuid;

        private Integer planEndOn;
        private String title;
        private Integer tye;
        private String content;
        private Integer condition;
        private Integer status;
        private String attachmentMd5List;
        private Integer clientCreateAt;
        private Integer updateAt;

        private List<String> areaPathName;
        private List<String> categoryPathName;
        private List<String> checkItemPathName;
        private List<String> attachmentUrlList;

    }

}
