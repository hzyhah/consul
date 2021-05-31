package com.cs.apiSer.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.cs.api.provider.IDoctorProvider;
import com.cs.apiSer.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class DoctorProvider implements IDoctorProvider {

    @Autowired
    IDoctorService doctorService;

    public String list() {
        System.out.println(JSON.toJSONString(doctorService.list()));
        return doctorService.list().toString();
    }
}
