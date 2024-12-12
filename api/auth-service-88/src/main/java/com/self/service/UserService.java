package com.self.service;

import pojo.User;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
public interface UserService {
    User createUser(String mobile, String nickname);

}
