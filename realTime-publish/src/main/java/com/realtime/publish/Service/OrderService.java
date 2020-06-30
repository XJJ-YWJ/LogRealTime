package com.realtime.publish.Service;

import java.util.Map;

public interface OrderService {

    //获取订单交易总额
    Double getOrderAmount(String date);

    //获取分时订单交易总额
    Map<Integer,Double> getOrderAmontHourMap(String date);
}
