package com.qwwuyu.recite.bean;

/**
 * 网络请求返回状态属性
 */
public class ResponseBean<T> {
    /** 请求成功请求码 */
    public final static String RESPONSE_STATUS_SUCCESS = "1";
    /** 出问题请求码 */
    public final static String RESPONSE_STATUS_ERROR = "-100";
    /** 返回状态码 */
    private String status;
    /** 返回消息 */
    private String info;
    /** 返回数据 */
    private T object;

    public ResponseBean() {
    }

    public String getStatus() {
        return status;
    }

    public int getStatusCode() {
        try {
            return Integer.parseInt(status);
        } catch (Exception e) {
            return -100;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusOK() {
        this.status = RESPONSE_STATUS_SUCCESS;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public static <T> ResponseBean<T> getErrorBean() {
        ResponseBean<T> bean = new ResponseBean<>();
        bean.status = RESPONSE_STATUS_ERROR;
        bean.info = "";
        return bean;
    }

    public static <T> ResponseBean<T> getOkBean() {
        ResponseBean<T> bean = new ResponseBean<>();
        bean.status = RESPONSE_STATUS_SUCCESS;
        bean.info = "";
        return bean;
    }
}
