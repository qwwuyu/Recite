package com.qwwuyu.recite.config;

/**
 * 广播事件
 * Created by qiwei on 2016/6/26.
 */
public class BroadcastFilters {
    // ***************************注册广播接收动作:包名+字段名 ***************************//
    /** 关闭所有ACT */
    public static final String ACTION_ACT_FINISH = "com.qwwuyu.recite.act_finish";
    /** 登录冲突 */
    public static final String ACTION_LOGIN_CONFLICT = "com.qwwuyu.recite.login_conflict";
}
