package com.cs.apiSer.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cs.apiSer.service.IRoleService;
import com.cs.apiSer.vo.User;
import com.cs.apiSer.service.IUserService;
import com.cs.common.PageBean;
import com.cs.api.provider.IAccountProvider;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AccountProvider implements IAccountProvider {

    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    public User login(String account, String password) {
        return userService.login(account, password);
    }

    public PageBean getList(int page, int pageSize, Map<String,Object> query){
        //userService.getList(page,pageSize,query);
        IPage iPage = userService.getList(page,pageSize,query);
        PageBean pageBean = new PageBean();
        pageBean.setAllRow(iPage.getTotal());
        pageBean.setCurrentPage(iPage.getCurrent());
        pageBean.setTotalPage(iPage.getPages());
        pageBean.setPageSize(iPage.getSize());
        List<User> users = iPage.getRecords();
        List list = new ArrayList();
        for (User user:users){
            com.cs.api.pojo.User user1 = new com.cs.api.pojo.User();
            BeanUtils.copyProperties(user,user1);
            list.add(user1);
        }
        pageBean.setList(list);
        return pageBean;
    }

    @Override
    public void unableUser(long id) {
        User user = userService.getById(id);
        user.setDeleteStatus(true);
        userService.updateById(user);
    }

    @Override
    public User findUser(long id) {
        return userService.getById(id);
    }

    @Override
    public void update(com.cs.api.pojo.User user) {
        User user1 = new User();
        BeanUtils.copyProperties(user,user1);
        userService.update(user1,null);
    }



}
