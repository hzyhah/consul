package com.cs.apiSer.provider;

import com.cs.api.pojo.AccessAuth;
import com.cs.api.provider.IAccessAuthProvider;
import com.cs.apiSer.service.IAccessAuthService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AccessAuthProvider implements IAccessAuthProvider {

    @Autowired
    IAccessAuthService accessAuthService;

    @Override
    public void registerAccess(AccessAuth accessAuth) {
        com.cs.apiSer.vo.AccessAuth accessAuth1 = new com.cs.apiSer.vo.AccessAuth();
        BeanUtils.copyProperties(accessAuth,accessAuth1);
        accessAuthService.save(accessAuth1);
    }

    @Override
    public AccessAuth getAccessAuth(String sub) {
        com.cs.apiSer.vo.AccessAuth auth =accessAuthService.getBySub(sub);
        if (auth!=null){
            AccessAuth accessAuth = new AccessAuth();
            BeanUtils.copyProperties(auth,accessAuth) ;
            return accessAuth;
        }
        return null;
    }

    @Override
    public void cancel(String sub) {

    }
}
