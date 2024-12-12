package com.self.service;

import bo.ModifyUserBO;
import pojo.User;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
public interface UserService {
    /**
     * 修改用户信息
     */
    void modifyUserInfo(ModifyUserBO userBO);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    User getById(String userId);
}
