package com.nettyUP.storage;

import com.alibaba.fastjson.JSON;
import com.nettyUP.storage.table.ChannelUserInfo;
import com.nettyUP.storage.table.NodeChannelsInfo;
import com.nettyUP.storage.table.NodeRegisterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

public class RedisStorage implements StorageTools {

    @Autowired
    Jedis jedis;

    public RedisStorage(){}

    public RedisStorage(Jedis jedis) {
        this.jedis = jedis;
    }

    public static String registerId = null;

    @Override
    public String nodeRegister(NodeRegisterInfo nodeRegisterInfo) {
        registerId = this.getNodeId();
        nodeRegisterInfo.setNodeId(registerId);
        jedis.sadd(getNodeRegisterKey(), JSON.toJSONString(nodeRegisterInfo));
        return registerId;
    }

    @Override
    public void nodeUnRegister(String... vals) {
        jedis.srem(getNodeRegisterKey(),vals);
    }

    @Override
    public void addUser(String uid, NodeChannelsInfo nodeChannelsInfo) {
        String nameSpace = !nodeChannelsInfo.getServiceName().equalsIgnoreCase("")?nodeChannelsInfo.getServiceName()+"_":"";
        String key = getUserRegisterKeyStr()+nameSpace+uid;
        jedis.sadd(key,JSON.toJSONString(nodeChannelsInfo));
    }

    @Override
    public void bindUserChannel(String nodeId, ChannelUserInfo channelUserInfo) {
        String key = getChannelusersRegisterKeyStr()+nodeId;
        jedis.sadd(key,JSON.toJSONString(channelUserInfo));
    }


}
