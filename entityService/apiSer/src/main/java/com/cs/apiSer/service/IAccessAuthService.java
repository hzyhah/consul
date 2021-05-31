package com.cs.apiSer.service;

import com.cs.apiSer.vo.AccessAuth;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houzy
 * @since 2021-05-28
 */
public interface IAccessAuthService extends IService<AccessAuth> {

    public AccessAuth getBySub(String sub);
}
