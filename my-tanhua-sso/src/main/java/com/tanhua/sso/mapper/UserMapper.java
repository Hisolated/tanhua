package com.tanhua.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.sso.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @Entity com.tanhua.sso.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
