package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.CheckItemMapper;
import com.longfor.longjian.houseqm.domain.internalService.CheckItemService;
import com.longfor.longjian.houseqm.po.CheckItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */
@Service
@Slf4j
public class CheckItemServiceImpl implements CheckItemService {
    @Resource
    CheckItemMapper checkItemMapper;
    @Override
    @LFAssignDataSource("zhijian2")
    public List<CheckItem> SearchCheckItemByKeyIn(List<String> keys) {
        return checkItemMapper.searchCheckItemByKeyIn( keys);
    }
}
