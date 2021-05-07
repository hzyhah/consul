package com.cs.im.message;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS
)
public abstract class Message implements Serializable {

    public Message(){}

    public int sequenceId;
    public int orderType;

    public static final String MAGIC_VAL = "UP.CX";

     abstract int get_OrderType();

     abstract  int get_sequenceId();

     abstract String get_Content();

    //登陆类型
     static final int LoginRequestOpType = 0;
    static final int LoginResponseOpType = 1;
    static final int chatSendOpType = 2;
    static final int chatReceiveOpType = 3;

}
