package com.self.service;

import bo.ModifyUserBO;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.self.excptions.MyCustomException;
import com.self.mapper.UserMapper;
import com.self.utils.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pojo.User;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static com.self.config.BaseInfoProperties.REDIS_USER_ALREADY_UPDATE_WECHAT_NUM;
import static com.self.grace.result.ResponseStatusEnum.USER_INFO_UPDATED_ERROR;
import static com.self.grace.result.ResponseStatusEnum.WECHAT_NUM_ALREADY_MODIFIED_ERROR;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
@Service
public class UserServiceImpl implements UserService{
    @Resource
    private RedisOperator redisOperator;
    @Resource
    private UserMapper userMapper;
    @Override
    public void modifyUserInfo(ModifyUserBO userBO) {
        User pendingUser = new User();
        String userId = userBO.getUserId();
        String wechatNum = userBO.getWechatNum();
        if(StringUtils.isBlank(userId)){
            throw new MyCustomException(USER_INFO_UPDATED_ERROR);
        }
        if(StringUtils.isNotBlank(wechatNum)){
            String isExist = redisOperator.get(REDIS_USER_ALREADY_UPDATE_WECHAT_NUM + ":" + userId);
            if(StringUtils.isNotBlank(isExist)){
                throw new MyCustomException(WECHAT_NUM_ALREADY_MODIFIED_ERROR);
            }

        }
        pendingUser.setId(userId);
        pendingUser.setUpdatedTime(LocalDateTime.now());
        BeanUtils.copyProperties(userBO,pendingUser);
        userMapper.updateById(pendingUser);


        //存储redis中标识（用户修改微信号  一年只能修改一次）
        if(StringUtils.isNotBlank(wechatNum)){
            redisOperator.setByDays(REDIS_USER_ALREADY_UPDATE_WECHAT_NUM+":"+userId,userId,365);
        }
    }

    @Override
    public User getById(String userId) {
        return userMapper.selectById(userId);
    }
}
