package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.CheckItemV3Mapper;
import com.longfor.longjian.houseqm.domain.internalService.CheckItemV3Service;
import com.longfor.longjian.houseqm.po.CheckItemV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 17:23
 */
@Service
@Slf4j
public class CheckItemV3ServiceImpl implements CheckItemV3Service {

    @Resource
    CheckItemV3Mapper checkItemV3Mapper;

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     *  * @param checkItems
     * @return java.util.List<com.longfor.longjian.houseqm.po.CheckItemV3>
     */
    @LFAssignDataSource("zhijian2")
    @Override
    public List<CheckItemV3> searchCheckItemyV3ByKeyInAndNoDeleted(List<String> checkItems) {
        return checkItemV3Mapper.selectCheckItemyV3ByKeyInAndNoDeleted(checkItems,"false");
    }
}
