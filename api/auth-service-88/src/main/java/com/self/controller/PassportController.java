package com.self.controller;

import bo.RegistLoginBO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.self.grace.result.GraceJSONResult;
import com.self.mapper.UsersMapper;
import pojo.User;
import com.self.service.UserService;
import com.self.task.SMSTask;
import com.self.utils.IPUtil;
import com.self.utils.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.UUID;

import static com.self.config.BaseInfoProperties.*;
import static com.self.grace.result.ResponseStatusEnum.*;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/3
 */
@RequestMapping("/passport")
@RestController
@Slf4j
@RequiredArgsConstructor
public class PassportController {

    private final RedisOperator redisOperator;
    private final SMSTask smsTask;
    private final UsersMapper usersMapper;
    private final UserService usersService;

    @PostMapping("logout")
    public GraceJSONResult logout(@RequestParam String userId) {
        //清理用户分布式会话
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        return GraceJSONResult.ok();
    }

    @PostMapping("login")
    public GraceJSONResult login(@RequestBody @Valid RegistLoginBO registLoginBO) {
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();
        //1.从redis中获取验证码 校验判断是否匹配
        String redisCode = redisOperator.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(smsCode)) {
            return GraceJSONResult.errorCustom(SMS_CODE_ERROR);
        }
        //2. 根据mobile 查询数据库 如果存在 直接下一步
        User user = usersMapper.selectOne(new QueryWrapper<User>().eq("mobile", mobile));
        if (user == null) {
            //2.1 用户未注册  直接返回错误
            return GraceJSONResult.errorCustom(USER_NOT_EXIST_ERROR);
        }
        //4.设置用户分布式会话  保存token 令牌 存储redis
        String uToken = TOKEN_USER_PREFIX + SYMBOL_DOT + UUID.randomUUID();
        redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);
        //5.返回用户数据给前端
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setCreateTime(user.getCreatedTime());
        userVO.setUpdateTime(user.getUpdatedTime());
        userVO.setUserToken(uToken);
        return GraceJSONResult.ok(userVO);

    }

    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {
        //安全校验
        if (StringUtils.isBlank(mobile)) {
            return GraceJSONResult.error();
        }
        //获取用户的ip（手机号）
        String userIp = IPUtil.getRequestIp(request);
        //限制该用户的ip在60s内只能获取一次验证码[语句仅是不重复的基础上存入key,具体的防刷在拦截器]
        redisOperator.setnx60s(MOBILE_SMSCODE + ":" + userIp, mobile);

        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        smsTask.sendSMSInTask(mobile, code);
        //将验证码存redis 用于后续注册、登录的校验
        redisOperator.set(MOBILE_SMSCODE + ":" + mobile, code, 10 * 60);
        return GraceJSONResult.ok();
    }

    @PostMapping("regist")
    public GraceJSONResult regist(@RequestBody RegistLoginBO registLoginBO,
                                  HttpServletRequest request) throws Exception {
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();
        String nickname = registLoginBO.getNickname();
        //1.从redis中获取验证码进行校验 判断是否匹配
        String redisCode = redisOperator.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(smsCode)) {
            return GraceJSONResult.errorCustom(SMS_CODE_ERROR);
        }
        //2. 根据mobile查询数据库 如果用户存在 则提示不能重复注册
        User user = usersMapper.selectOne(new QueryWrapper<User>().eq("mobile", mobile));
        if (user == null) {
            //2.1 如果为null 则注册
            user = usersService.createUser(mobile, nickname);
        } else {
            //2.2重复 则提示不可重复注册
            return GraceJSONResult.errorCustom(USER_ALREADY_EXIST_ERROR);
        }
        //3.用户注册成功后 删除redis中的短信验证码
        redisOperator.del(MOBILE_SMSCODE + ":" + mobile);

        //4.设置用户分布式护花 保存token 令牌 存储到redis
        String uToken = TOKEN_USER_PREFIX + SYMBOL_DOT + UUID.randomUUID();
        redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);

        //5.返回用户数据给前端
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(uToken);
        return GraceJSONResult.ok(userVO);
    }
}
