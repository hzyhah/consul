package com.cs.common;


import com.cs.common.json.JsonUtils;

public class Result<T> {
    private String message;
    private int retCode;
    private T data;

    private Result() {
        this.retCode = 1;
        this.message = "";
    }

    private Result(T data) {
        this.retCode = 1;
        this.message = "";
        this.data = data;
    }
    private Result(CodeMsg cm) {
        if(cm == null){
            return;
        }
        this.retCode = cm.getRetCode();
        this.message = cm.getMessage();
        this.data = (T) new String("");
    }

    /**
     * 成功时候的调用
     * @return
     */
    public static <T> String ok(T data){
        return JsonUtils.toJsonString(new Result<T>(data));
    }
    /**
     * 成功时候的调用
     * @return
     */
    public static String ok(){
        return JsonUtils.toJsonString(new Result());
    }
    /**
     * 成功时候的调用
     * @return
     */
    public static <T> Result<T> success(T data){
        return  new Result<T>(data);
    }
    /**
     * 成功，不需要传入参数
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> success(){
        return (Result<T>) success("");
    }

    /**
     * 失败时候的调用
     * @return
     */
    public static String fail(CodeMsg cm){
        return  JsonUtils.toJsonString(new Result(cm));
    }

    /**
     * 失败时候的调用
     * @return
     */
    public static String error(CodeMsg cm){
        return  JsonUtils.toJsonString(new Result(cm));
    }

    /**
     * 失败时候的调用,扩展消息参数
     * @param cm
     * @param msg
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm,String msg){
        cm.setMessage(cm.getMessage()+"--"+msg);
        return new Result<T>(cm);
    }
    public T getData() {
        return data;
    }
    public String getMessage() {
        return message;
    }
    public int getRetCode() {
        return retCode;
    }
}