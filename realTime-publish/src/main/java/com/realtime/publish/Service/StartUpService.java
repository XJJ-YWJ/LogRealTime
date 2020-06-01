package com.realtime.publish.Service;

import java.util.Map;

public interface StartUpService{

    //获取日活跃总数
    Long getTotal(String date);

    //获取各个时段的日活跃用户
    Map<String,Long> getHourTotal(String date);


}
