package com.cs.apiSer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs.apiSer.vo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houzy
 * @since 2021-04-28
 */
public interface IRoleService extends IService<Role> {
    List<Role> getListByUId(long uid);

}
