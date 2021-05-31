package com.nettyUP.storage.table;

import java.io.Serializable;

public class ChannelUserInfo  implements Serializable {

    String uid;

    String serviceName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
