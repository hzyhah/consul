package com.cs.apiSer.provider;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs.api.pojo.User;
import com.cs.api.provider.IAccountProvider;
import com.cs.apiSer.service.IUserService;
import com.cs.common.PageBean;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Service
public class AccountProvider implements IAccountProvider {

    @Autowired
    IUserService userService;

    public User login(String account, String password) {
        return userService.login(account, password);
    }

    public PageBean getList(int page, int pageSize, Map<String,Object> query){
        return userService.getList(page,pageSize,query);
    }

}
