package com.longfor.longjian.houseqm.app.vo.issuelist;

import com.longfor.longjian.houseqm.app.vo.IssueListVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issuelist
 * @ClassName: IssueListRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/19 13:34
 */
@Data
@NoArgsConstructor
public class IssueListRsp implements Serializable {

    private Integer total;
    private List<IssueListVo> issue_list;

}
