package com.self.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pojo.User;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author jcy
 * @since 2024-12-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
