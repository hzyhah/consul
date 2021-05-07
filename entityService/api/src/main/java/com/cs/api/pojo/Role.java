package com.cs.api.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Role  implements Serializable {
    private String roleName;

    private String roleAilas;

    private Integer status;

    public Long id;

    public Date addTime;
}
