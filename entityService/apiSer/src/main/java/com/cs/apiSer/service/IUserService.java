package com.cs.apiSer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cs.apiSer.vo.User;
import com.cs.common.PageBean;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houzy
 * @since 2021-04-18
 */
public interface IUserService extends IService<User> {
    public User login(String account,String password);
    public User saveCascade(User user);
    public PageBean getList(int page, int pageSize, Map<String,Object> query);

}
