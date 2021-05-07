package com.cs.apiSer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.apiSer.dao.AuthMapper;
import com.cs.apiSer.service.IAuthService;
import com.cs.apiSer.vo.Auth;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements IAuthService {

}
