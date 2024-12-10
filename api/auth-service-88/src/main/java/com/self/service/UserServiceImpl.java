package com.self.service;

import com.self.enums.Sex;
import com.self.mapper.UsersMapper;
import com.self.pojo.User;
import com.self.utils.DesensitizationUtil;
import com.self.utils.LocalDateUtils;
import com.self.utils.SnowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author jcy
 * @version 1.0
 * @data 2024/12/10
 */
@Service
public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;
//    private static final String USER_FACE1 = "http://127.0.0.1:9000/itzixi/face/1749619640390205441/e9f2be46-56ba-454c-a7ee-3e290bad6a59.jpg";

    public UserServiceImpl(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Override
    public User createUser(String mobile, String nickname) {
        User user = new User();

        user.setMobile(mobile);
        user.setId(SnowUtils.getId().toString());
        String uuid = UUID.randomUUID().toString();

        String[] uuidStr = uuid.split("-");
        String wechatNum="wx"+uuidStr[0]+uuidStr[1];
        user.setWechatNum(wechatNum);

//        String wechatNumUrl =getQrCodeUrl(wechatNum,TEMP_STRING);
        user.setWechatNumImg("wechatNumUrl");

        if(StringUtils.isBlank(nickname)){
            user.setNickname("用户"+ DesensitizationUtil.commonDisplay(mobile));
        }
        user.setRealName("");
        user.setSex(Sex.secret.type);
//        user.setFace(USER_FACE1);
        user.setFace("USER_FACE1");
//        user.setFriendCircleBg(USER_FACE1);
        user.setEmail("");
        user.setNickname(nickname);

        user.setBirthday(LocalDateUtils.parseLocalDate("1980-01-01",LocalDateUtils.DATE_PATTERN));

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");

        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        usersMapper.insert(user);
        return user;
    }
}
