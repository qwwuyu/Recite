package com.qwwuyu.recite.config;

/**
 * 静态字段
 * Created by qiwei on 2016/8/4.
 */
public class FinalConfig {
    /* ***************************** 常用简单的intent的传参key ***************************** */
    /** 字符串数据1 */
    public static final String it_str = "it_str";
    /** 字符串数据2 */
    public static final String it_str2 = "it_str2";
    /** 字符串数据3 */
    public static final String it_str3 = "it_str3";
    /** boolean */
    public static final String it_bl = "it_bl";
    /** Integer ID */
    public static final String it_id = "it_id";
    /** Integer 类型 */
    public static final String it_type = "it_type";
    /** 数据Bean */
    public static final String it_bean = "it_bean";
    /** 数据Bean2 */
    public static final String it_bean2 = "it_bean2";
    /** 数据集合 */
    public static final String it_beans = "it_beans";
    /* ***************************** EventBus 0~999 ****************************** */
    /** Test */
    public static final int EVENT_TEST = 0;
    /** 关闭所有ACT */
    public static final int EVENT_ACT_FINISH = 1;
    /** 今天改变 */
    public static final int EVENT_NOW_DAY_CHANGES = 2;

    /* ***************************** 请求码1000~1999 ****************************** */
    /** 通用的 */
    public static final int REQUEST_COMM = 1000;
    /** 请求登录 */
    public static final int REQUEST_LOGIN = 1001;

    /* ***************************** 结果码2000~2999 ****************************** */
    /** 通用的 */
    public static final int RESULT_COMM = 2000;
    /** 请求登录 */
    public static final int RESULT_LOGIN = 2001;

}
