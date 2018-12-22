package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.CheckItemV3;

import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 16:58
 */
public interface CheckItemV3Service {

    List<CheckItemV3> searchCheckItemyV3ByKeyInAndNoDeleted(List<String> checkItems);
}
