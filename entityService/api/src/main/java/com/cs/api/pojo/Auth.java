package com.cs.api.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Auth implements Serializable {
    private String type;

    private String val;

    private Integer status;

    public Long id;

    public Date addTime;
}
