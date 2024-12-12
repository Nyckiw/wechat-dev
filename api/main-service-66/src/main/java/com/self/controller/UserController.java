package com.self.controller;

import bo.ModifyUserBO;
import com.self.grace.result.GraceJSONResult;
import com.self.service.UserService;
import com.self.utils.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import pojo.User;
import vo.UserVO;

import java.util.UUID;

import static com.self.config.BaseInfoProperties.*;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("userinfo")
public class UserController {
    private final UserService userService;
    private final RedisOperator redisOperator;

    @PostMapping("modify")
    public GraceJSONResult modify(@RequestBody ModifyUserBO userBO) {
        //修改用户信息
        userService.modifyUserInfo(userBO);
        //返回最新的用户信息
        UserVO userVO = getUserInfo(userBO.getUserId(), true);

        return GraceJSONResult.ok(userVO);
    }

    private UserVO getUserInfo(String userId, boolean needToken) {
        //获取用户的的最新信息
        User lastUser = userService.getById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(lastUser, userVO);
        //todo 时间类可能手动复制
        if (needToken) {
            String uToken = TOKEN_USER_PREFIX + SYMBOL_DOT + UUID.randomUUID();
            redisOperator.set(REDIS_USER_TOKEN + ":" + userId, uToken);
            userVO.setUserToken(uToken);
        }
        return userVO;
    }
    @PostMapping("get")
    public GraceJSONResult get(@RequestParam String userId){
        return GraceJSONResult.ok(getUserInfo(userId,false));
    }
}
