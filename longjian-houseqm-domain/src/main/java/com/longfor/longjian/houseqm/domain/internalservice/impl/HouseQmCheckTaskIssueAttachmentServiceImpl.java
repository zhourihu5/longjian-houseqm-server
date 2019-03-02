package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseQmCheckTaskIssueAttachmentMapper;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskIssueAttachmentService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueAttachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/11 0011 19:38
 */
@Service
@Slf4j
public class HouseQmCheckTaskIssueAttachmentServiceImpl implements  HouseQmCheckTaskIssueAttachmentService{

    @Resource
    HouseQmCheckTaskIssueAttachmentMapper houseQmCheckTaskIssueAttachmentMapper;


    
    @LFAssignDataSource("zhijian2")
    public int inseretBatch(List<HouseQmCheckTaskIssueAttachment> attachements) {
        int result=0;
        for (HouseQmCheckTaskIssueAttachment item : attachements) {
            item.setCreateAt(new Date());
            item.setUpdateAt(new Date());
            result+=houseQmCheckTaskIssueAttachmentMapper.insertSelective(item);

        }
        return result;
    }

    
    @LFAssignDataSource("zhijian2")
    //@Transactional
    public int deleteByIssueUuidMd5(String issueUuid, String md5) {
        Example example = new Example(HouseQmCheckTaskIssueAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid",issueUuid).andEqualTo("md5",md5);
        HouseQmCheckTaskIssueAttachment attachment = new HouseQmCheckTaskIssueAttachment();

        attachment.setDeleteAt(new Date());
        attachment.setUpdateAt(new Date());
        attachment.setIssueUuid(issueUuid);
        attachment.setMd5(md5);

        return houseQmCheckTaskIssueAttachmentMapper.updateByExampleSelective(attachment,example);
    }

    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAttachment> searchByIssueUuid(Set<String> issueUuids){
        if (CollectionUtils.isEmpty(issueUuids))return Lists.newArrayList();
        List<HouseQmCheckTaskIssueAttachment> houseQmCheckTaskIssueAttachments = houseQmCheckTaskIssueAttachmentMapper.selectByIssueUuid(issueUuids, "false");
        return houseQmCheckTaskIssueAttachments;
    }

    
    @LFAssignDataSource("zhijian2")
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

    
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAttachment> selectByIssueUuidAndpublicTypeAndattachmentTypeAndNotDel(String issueUuid, Integer value, Integer value1) {
        Example example = new Example(HouseQmCheckTaskIssueAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid",issueUuid);
        criteria.andEqualTo("publicType",value);
        criteria.andEqualTo("attachmentType",value1);
        criteria.andIsNull("deleteAt");
        return  houseQmCheckTaskIssueAttachmentMapper.selectByExample(example);
    }

    
    @LFAssignDataSource("zhijian2")
    //@Transactional
    public int add(HouseQmCheckTaskIssueAttachment value) {
        value.setCreateAt(new Date());
        value.setUpdateAt(new Date());
        return    houseQmCheckTaskIssueAttachmentMapper.insert(value);

    }

    
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssueAttachment selectByMd5AndNotDel(Object attachment) {
        Example example = new Example(HouseQmCheckTaskIssueAttachment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("md5",attachment);

        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskIssueAttachmentMapper.selectOneByExample(example);
    }

}
