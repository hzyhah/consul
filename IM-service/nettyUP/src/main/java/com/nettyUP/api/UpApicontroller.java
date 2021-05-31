package com.nettyUP.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpApicontroller {

    /**
     * netty节点注册上线
     */
    @RequestMapping("/node_register")
    public String register(String regInfo){
        return "ok";
    }

    /**
     * netty 节点下线
     * @param regId 注册id
     * @return
     */
    @RequestMapping("/node_unregister")
    public String unregister(String regId){
        return "ok";
    }

    /**
     * 为每一个neety client上的channel建立远程session
     * @param regId
     * @param cahnnelInfo
     * @return
     */
    @RequestMapping("/registerChannel")
    public String registerChannel(String regId,String cahnnelInfo){
        return "ok";
    }


    /**
     * 为每一个neety client上的channel建立远程session
     * @param regId
     * @param cahnnelInfo
     * @return
     */
    @RequestMapping("/unregisterChannel")
    public String unregisterChannel(String regId,String cahnnelInfo){
        return "ok";
    }
}
