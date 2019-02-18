package com.longfor.longjian.houseqm.app.vo.houseqmissue;

import com.longfor.longjian.common.consts.ReqParamCheckErrors;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmissue
 * @ClassName: HouseqmCheckTaskIssueIndexJsonReqMsg
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 16:23
 */
@Data
@NoArgsConstructor
public class HouseqmCheckTaskIssueIndexJsonReqMsg implements Serializable {
    @NotNull(message = ReqParamCheckErrors.PARAM_IS_NULL)
    private String safeCallKey;
    @NotNull(message = ReqParamCheckErrors.PARAM_IS_NULL)
    private Integer category_cls;//模块类型
    @NotNull(message = ReqParamCheckErrors.PARAM_IS_NULL)
    private Integer project_id;//项目ID
    private Integer task_id;//任务ID
    private String task_name;//任务名
    private String category_key;//任务类型
    private String check_item_key;//检查项类型
    private List<Integer> area_ids;//区域ID
    private List<Integer> status_in;//问题状态
    private Integer checker_id;//检查人ID
    private Integer repairer_id;//整改人ID
    private Integer type;//问题类型
    private Integer condition;//严重程度
    private String create_on_begin;//开始时间范围
    private String create_on_end;//结束时间范围
    private Boolean is_overdue;//是否超期
    private List<String> uuids;//问题uuid记录列表
}
