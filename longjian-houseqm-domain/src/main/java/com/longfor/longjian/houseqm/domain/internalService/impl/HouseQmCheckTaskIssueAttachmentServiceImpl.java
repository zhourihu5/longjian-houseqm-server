package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseQmCheckTaskIssueAttachmentMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueAttachmentService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueAttachment;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/11 0011 19:38
 */
@Service
@Slf4j
public class HouseQmCheckTaskIssueAttachmentServiceImpl implements HouseQmCheckTaskIssueAttachmentService {

    @Resource
    HouseQmCheckTaskIssueAttachmentMapper houseQmCheckTaskIssueAttachmentMapper;


    @Override
    @LFAssignDataSource(value = "zhijian2")
    @Transactional
    public int inseretBatch(List<HouseQmCheckTaskIssueAttachment> attachements) {
        return houseQmCheckTaskIssueAttachmentMapper.insertList(attachements);
    }

    @Override
    @LFAssignDataSource(value = "zhijian2")
    @Transactional
    public int deleteByIssueUuidMd5(String issueUuid, String md5) {
        Example example = new Example(HouseQmCheckTaskIssueAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid",issueUuid).andEqualTo("md5",md5);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskIssueAttachmentMapper.deleteByExample(example);
    }

    /**
     * 根据问题uuid 查 取 未删除的 数据
     * @param issueUuids
     * @return
     */
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueAttachment> searchByIssueUuid(Set<String> issueUuids){
        List<HouseQmCheckTaskIssueAttachment> houseQmCheckTaskIssueAttachments = houseQmCheckTaskIssueAttachmentMapper.selectByIssueUuid(issueUuids, "false");
        return houseQmCheckTaskIssueAttachments;
    }

    @Override
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueAttachment> selectByissueUuidAnduserIdAndpublicTypeAndattachmentTypeAndNotDel(String issueUuid,Integer uid, Integer value, Integer value1) {
        Example example = new Example(HouseQmCheckTaskIssueAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid",issueUuid);
        criteria.andEqualTo("userId",uid);
        criteria.andEqualTo("publicType",value);
        criteria.andEqualTo("attachmentType",value1);
        criteria.andIsNull("deleteAt");
        return  houseQmCheckTaskIssueAttachmentMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueAttachment> selectByIssueUuidAndpublicTypeAndattachmentTypeAndNotDel(String issueUuid, Integer value, Integer value1) {
        Example example = new Example(HouseQmCheckTaskIssueAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid",issueUuid);
        criteria.andEqualTo("publicType",value);
        criteria.andEqualTo("attachmentType",value1);
        criteria.andIsNull("deleteAt");
        return  houseQmCheckTaskIssueAttachmentMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource(value = "zhijian2")
    @Transactional
    public int add(HouseQmCheckTaskIssueAttachment value) {
        return    houseQmCheckTaskIssueAttachmentMapper.insert(value);

    }

    @Override
    @LFAssignDataSource(value = "zhijian2")
    public HouseQmCheckTaskIssueAttachment selectByMd5AndNotDel(Object attachment) {
        Example example = new Example(HouseQmCheckTaskIssueAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("md5",attachment);

        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskIssueAttachmentMapper.selectOneByExample(example);
    }

}
