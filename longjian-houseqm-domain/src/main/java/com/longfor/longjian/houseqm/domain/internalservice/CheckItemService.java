package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.CheckItem;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */

public interface CheckItemService {
    List<CheckItem> searchCheckItemByKeyIn(List<String> keys);
}
