package com.qwwuyu.recite.bean;

import android.os.Bundle;

/**
 * EventBus的传递对象
 * Created by qiwei on 2016/4/8.
 */
public class EventBean {
    /** 标识判断 */
    private int what;
    /** 简单数据 */
    private Object obj;
    /** 复杂数据 */
    private Bundle bundle;

    public EventBean(int what) {
        this.what = what;
    }

    public EventBean(int what, Object obj, Bundle bundle) {
        this.what = what;
        this.bundle = bundle;
        this.obj = obj;
    }

    public EventBean(int what, Bundle bundle) {
        this.bundle = bundle;
        this.what = what;
    }

    public EventBean(int what, Object obj) {
        this.what = what;
        this.obj = obj;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public <T> T getObj() {
        return (T) obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
