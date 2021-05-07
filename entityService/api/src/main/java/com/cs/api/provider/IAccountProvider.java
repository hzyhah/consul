package com.cs.api.provider;


import com.cs.api.pojo.User;
import com.cs.common.PageBean;

import java.util.Map;

public interface IAccountProvider {
    public User login(String account, String password);
    public PageBean getList(int page, int pageSize, Map<String,Object> query);
}
