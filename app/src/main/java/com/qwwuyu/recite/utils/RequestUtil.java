package com.qwwuyu.recite.utils;


import com.qwwuyu.recite.bean.ResponseBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by qiwei on 2016/4/7.
 */
public class RequestUtil<T> {
    /**
     * 检查请求是否成功
     */
    private void isResponseOk(String response, ResponseBean<T> bean) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        int request_result = jsonObject.optInt("request_result", -100);
        if (request_result != -100) {
            bean.setStatus(String.valueOf(request_result));
        }
        int REQUEST_RESULT = jsonObject.optInt("REQUEST_RESULT", -100);
        if (REQUEST_RESULT != -100) {
            bean.setStatus(String.valueOf(REQUEST_RESULT));
        }
        if (request_result != 1 && REQUEST_RESULT != 1) {
            throw new JSONException("");
        }
    }

    public ResponseBean<T> sendPost(String url, HashMap<String, Object> attribute, IParseJson<T> iParseJson) {
        ResponseBean<T> bean = ResponseBean.getErrorBean();
        try {
            //请求网络
            String body = new NetUtil().sendPost(url, attribute);
            LogUtil.i("body:" + body);
            //检查数据请求成功
            isResponseOk(body, bean);
            //暴露接口解析数据
            bean.setObject(iParseJson.parseJson(body));
            //所有处理完成
            bean.setStatusOK();
        } catch (IOException e) {
            e.printStackTrace();
            bean.setInfo("request_connect_error");
        } catch (TimeoutException e) {
            e.printStackTrace();
            bean.setInfo("request_time_out");
        } catch (Exception e) {
            e.printStackTrace();
            bean.setInfo("request_data_error");
        }
        return bean;
    }

    public interface IParseJson<T> {
        T parseJson(String json) throws JSONException;
    }
}
