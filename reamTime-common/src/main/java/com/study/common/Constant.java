package com.study.common;

/**
 * 保存全局的常量
 */
public class Constant {

    //kafka 启动日志 Topic
    public static final String KAFKA_TOPIC_STARTUP = "STARTUP";
    //kafka 时间日志 Topic
    public static final String KAFKA_TOPIC_EVENT = "EVENT";
    //kafka 订单日志 Topic
    public static final String KAFKA_TOPIC_ORDER = "ORDER";
    //ES index
    public static final String ES_INDEX_DAU = "real_time_dau";
    public static final String ES_INDEX_ORDER = "real_time_order";
}
