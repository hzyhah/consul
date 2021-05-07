package com.cs.im.protocol;

public class UpcxProtocol {
    /**
     * 魔数
     */
    public static final String MAGIC_VAL = "UP.CX";
    /**
     * 版本号
     */
    public static int VERSION = 0;
    /**
     * 序列化算法
     * 0 jdk  1 json 2 protobuf 3 hessian
     */
    public static int SER= 0;

    /**
     * 指令类型
     */
    public static int ORDER_TYPE = 0;

    /**
     * 请求序号
     */
    public static long sequenceId=0;

    /**
     * 消息长度
     */
    public static int MESAGE_LENGTH=0;

    /**
     * 消息正文
     */
    public static String msg="";
}
