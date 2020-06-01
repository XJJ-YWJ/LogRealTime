package com.realtime.publish.Controller;


import com.alibaba.fastjson.JSON;
import com.realtime.publish.Service.StartUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class StartUpController {

    @Autowired
    private StartUpService ss;

    @GetMapping("/realtime-total")
    public String getTotal(@RequestParam("date") String date){

        List<Map<String , Object>> list = new ArrayList<>();
        Map<String , Object> dauMap = new HashMap<>();
        dauMap.put("id","dau");
        dauMap.put("name","新增日活");
        dauMap.put("value",ss.getTotal(date));
        list.add(dauMap);

        return JSON.toJSONString(list);
    }


    @GetMapping("/realtime-hour")
    public String getHourTotal(@RequestParam("id") String id,@RequestParam("date") String date){

        if ("dau".equals(id)){
            Map<String, Long> todayDauMap = ss.getHourTotal(date);
            Map<String, Long> yesterdayDauMap = ss.getHourTotal(getYesterday(date));

            Map<String , Map<String,Long>> hourMap = new HashMap<>();
            hourMap.put("today",todayDauMap);
            hourMap.put("yesterday",yesterdayDauMap);

            return JSON.toJSONString(hourMap);
        }

        return null;
    }


    //获取指定日期前一天的日期
    private String getYesterday(String today){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String yes = "";
        try {
            Date date = format.parse(today);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DATE,c.get(Calendar.DATE)-1);

            yes = format.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yes;
    }

}
