package com.self.mapper;

import com.self.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author jcy
 * @since 2024-12-09
 */
@Mapper
public interface UsersMapper extends BaseMapper<User> {

}
