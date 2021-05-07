package com.cs.apiSer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.apiSer.vo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author houzy
 * @since 2021-04-28
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过用户id获取用户对应权限
     * @param uid
     * @return
     */
    @Select("select * from cs_role " +
            " left join " +
            " user_role on user_role.rid=cs_role.id " +
            " where  user_role.uid =#{uid} and cs_role.status =1 ")
    List<Role> getListByUId(long uid);

}
