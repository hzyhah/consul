package com.cs.consulApp.controller;

import com.cs.api.provider.IDoctorProvider;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoctorController {

    @Reference
    IDoctorProvider doctorProvider;

    @RequestMapping(value = "/findDoctor")
    public String findDoctor(){
        return  doctorProvider.list();
    }
}
