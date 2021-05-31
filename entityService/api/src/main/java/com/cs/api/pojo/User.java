package com.cs.api.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author houzy
 * @since 2021-04-18
 */
@Data
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    public int sex;

    public String name;

    public String nickName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Long id;

    public Date addTime;

    public String password;

    public String account;

    public Boolean deleteStatus = false;

}
