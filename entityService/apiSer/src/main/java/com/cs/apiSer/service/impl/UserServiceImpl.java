package com.cs.apiSer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.apiSer.dao.UserMapper;
import com.cs.apiSer.dao.UserRoleMapper;
import com.cs.apiSer.vo.Role;
import com.cs.apiSer.vo.User;
import com.cs.apiSer.service.IUserService;
import com.cs.apiSer.vo.UserRole;
import com.cs.common.PageBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houzy
 * @since 2021-04-18
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    UserMapper userMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    /**
     * 级联插入，主要插入中间表。
     * @param user
     * @return
     */
    public User saveCascade(User user){
        userMapper.insert(user);
        if (user.getRoles()!=null){
            Map map= new HashMap();
            map.put("uid",user.getId());
            userRoleMapper.deleteByMap(map);
            for (Role role:user.getRoles()) {
                UserRole userRole = new UserRole();
                userRole.setUid(user.getId());
                userRole.setRid(role.getId());
                userRoleMapper.insert(userRole);
            }
        }
        return user;
    }

    /**
     * 级联更新 主要维护中间表
     * @param user
     * @return
     */

    public User updateCascade(User user){
        userMapper.update(user,null);
        if (user.getRoles()!=null){
            Map map= new HashMap();
            map.put("uid",user.getId());
            userRoleMapper.deleteByMap(map);
            for (Role role:user.getRoles()) {
                UserRole userRole = new UserRole();
                userRole.setUid(user.getId());
                userRole.setRid(role.getId());
                userRoleMapper.insert(userRole);
            }
        }else{
            Map map= new HashMap();
            map.put("uid",user.getId());
            userRoleMapper.deleteByMap(map);
        }
        return user;
    }

    public User login(String account,String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        HashMap hashMap = new HashMap();
        hashMap.put("account",account);
        hashMap.put("password",password);
        queryWrapper.allEq(hashMap);
        return userMapper.selectOne(queryWrapper);
    }

    public PageBean getList(int page, int pageSize, Map<String, Object> query) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();

        if(query != null && query.size() >0 ){
            if (query.get("name")!=null){
                queryWrapper.likeRight("name",query.get("name").toString());
            }
        }
        queryWrapper.orderByDesc("addTime");

        Page<User> iPage = new Page<User>(page,pageSize);
        return  PageBean.init(userMapper.selectPage(iPage,queryWrapper));
    }

}
