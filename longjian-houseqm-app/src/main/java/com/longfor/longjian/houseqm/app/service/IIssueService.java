package com.longfor.longjian.houseqm.app.service;

import com.longfor.gaia.gfs.core.bean.PageInfo;
import com.longfor.longjian.houseqm.app.vo.IssueListVo;


/**
 * @author Houyan
 * @date 2018/12/21 0021 10:50
 */
public interface IIssueService {

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param projectId
     * @param categoryCls
     * @param taskId
     * @param categoryKey
     * @param checkItemKey
     * @param areaIds
     * @param statusIn
     * @param checkerId
     * @param repairerId
     * @param type
     * @param condition
     * @param keyWord
     * @param createOnBegin
     * @param createOnEnd
     * @param isOverDue
     * @param pageNum
     * @param pageSize
     * @return com.longfor.gaia.gfs.core.bean.PageInfo<com.longfor.longjian.houseqm.app.vo.IssueListVo>
     */
    PageInfo<IssueListVo> list(Integer projectId,String categoryCls,Integer taskId,String categoryKey,String checkItemKey,
                               String areaIds,String statusIn,Integer checkerId,Integer repairerId,Integer type,Integer condition,String keyWord,
                               String createOnBegin,String createOnEnd,Boolean isOverDue,Integer pageNum,Integer pageSize);

}
