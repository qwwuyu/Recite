package com.qwwuyu.recite.utils;

import com.qwwuyu.recite.bean.ResponseBean;

import org.json.JSONException;

import java.util.HashMap;


/**
 * 网络相关的业务,因为不多,不分类了
 */
public class NetBiz {
    private HashMap<String, Object> getPostHeadMap() {
        HashMap<String, Object> map = new HashMap<>();
        return map;
    }

    /**
     * 获取国家位子
     *
     * @return 国家位子数据
     */
    public ResponseBean<String> test() {
        HashMap<String, Object> attribute = getPostHeadMap();
        attribute.put("key", "value");
        RequestUtil.IParseJson<String> iParseJson = new RequestUtil.IParseJson<String>() {
            @Override
            public String parseJson(String response) throws JSONException {
                return response;
            }
        };
        return new RequestUtil<String>().sendPost("url", attribute, iParseJson);
    }
}
