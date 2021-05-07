package com.cs.apiSer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.apiSer.dao.RoleMapper;
import com.cs.apiSer.service.IRoleService;
import com.cs.apiSer.vo.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houzy
 * @since 2021-04-28
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    RoleMapper roleMapper;

    public List<Role> getListByUId(long uid) {
        return roleMapper.getListByUId(uid);
    }
}
