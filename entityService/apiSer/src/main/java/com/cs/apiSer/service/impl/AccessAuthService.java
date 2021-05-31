package com.cs.apiSer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.apiSer.dao.AccessAuthMapper;
import com.cs.apiSer.service.IAccessAuthService;
import com.cs.apiSer.vo.AccessAuth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houzy
 * @since 2021-05-28
 */
@Service
@Transactional
public class AccessAuthService extends ServiceImpl<AccessAuthMapper, AccessAuth> implements IAccessAuthService {

    @Resource
    AccessAuthMapper accessAuthMapper;

    @Override
    public AccessAuth getBySub(String sub) {
        QueryWrapper<AccessAuth> queryWrapper = new QueryWrapper<AccessAuth>();
        queryWrapper.eq("sub",sub);
        queryWrapper.eq("deleteStatus",false);
        return accessAuthMapper.selectOne(queryWrapper);
    }
}
