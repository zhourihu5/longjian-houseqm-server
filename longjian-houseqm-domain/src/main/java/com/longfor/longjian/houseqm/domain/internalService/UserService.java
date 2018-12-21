package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<Integer, User> selectByIds(List<Integer> users);

    List<User> searchByUserIdInAndNoDeleted(List<Integer> userIds);


}
