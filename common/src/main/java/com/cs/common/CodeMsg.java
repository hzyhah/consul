package com.cs.common;

public class CodeMsg {
    private int retCode;
    private String message;
    // 按照模块定义CodeMsg
    /**
     * retcode 0-200 接口能正常使用，但业务异常
     * 500---600 开发的 表示系统错误
     */
    public static CodeMsg ILLEGAL_REQUEST = new CodeMsg(-1,"非法请求");
    public static CodeMsg PARAMETER_ISNULL = new CodeMsg(-2,"输入参数为空");
    //授权 token 异常
    public static CodeMsg ILLEGAL_JTW = new CodeMsg(-3,"无效token");

    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    //业务错误
    public static CodeMsg LOGIN_FAILD = new CodeMsg(100001,"用户名或密码错误");

    // 系统错误
    public static CodeMsg SERVER_EXCEPTION = new CodeMsg(500000,"服务端异常");
    public static CodeMsg USER_NOT_EXSIST = new CodeMsg(500102,"用户不存在");
    public static CodeMsg ONLINE_USER_OVER = new CodeMsg(500103,"在线用户数超出允许登录的最大用户限制。");
    public static CodeMsg SESSION_NOT_EXSIST =  new CodeMsg(500104,"不存在离线session数据");
    public static CodeMsg NOT_FIND_DATA = new CodeMsg(500105,"查找不到对应数据");


    private CodeMsg(int retCode, String message) {
        this.retCode = retCode;
        this.message = message;
    }
    public int getRetCode() {
        return retCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}