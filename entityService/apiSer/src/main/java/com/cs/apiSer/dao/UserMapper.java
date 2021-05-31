package com.cs.apiSer.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cs.apiSer.vo.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author houzy
 * @since 2021-04-18
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from cs_user ${ew.customSqlSegment} ")
    @Results({
            @Result(column="id",property="id"),
            @Result(column="id",property="roles",
                    many=@Many(
                            select="com.cs.apiSer.dao.RoleMapper.getListByUId",fetchType = FetchType.LAZY
                    )
            )
    })
    IPage<User> selectPage(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);
}
