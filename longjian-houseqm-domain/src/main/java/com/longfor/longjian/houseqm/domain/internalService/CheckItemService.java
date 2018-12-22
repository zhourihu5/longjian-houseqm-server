package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.CheckItem;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */

public interface CheckItemService {
    List<CheckItem> SearchCheckItemByKeyIn(List<String> keys);
}
