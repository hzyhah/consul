package com.cs.api.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccessAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long sub;
    Date addTime;
    private String type;
    boolean deleteStatus;
    private String accessAuth;
    private String public_key;
    private String private_key;

}
