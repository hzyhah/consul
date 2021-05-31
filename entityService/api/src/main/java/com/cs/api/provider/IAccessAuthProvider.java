package com.cs.api.provider;


import com.cs.api.pojo.AccessAuth;

public interface IAccessAuthProvider {
    public void registerAccess(AccessAuth accessAuth);
    public AccessAuth getAccessAuth(String sub);
    public void cancel(String sub);
}
