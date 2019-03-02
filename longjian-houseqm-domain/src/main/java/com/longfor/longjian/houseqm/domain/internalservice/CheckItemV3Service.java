package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.CheckItemV3;

import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 16:58
 */
public interface CheckItemV3Service {

    List<CheckItemV3> searchCheckItemyV3ByKeyInAndNoDeleted(List<String> checkItems);

    CheckItemV3 selectByKeyNotDel(String checkItemKey);
}
