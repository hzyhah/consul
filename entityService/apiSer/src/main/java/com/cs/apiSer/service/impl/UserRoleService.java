package com.cs.apiSer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.apiSer.dao.UserRoleMapper;
import com.cs.apiSer.service.IUserRolesService;
import com.cs.apiSer.vo.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRolesService {
}
