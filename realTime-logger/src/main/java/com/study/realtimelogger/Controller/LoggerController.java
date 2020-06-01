package com.study.realtimelogger.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.study.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggerController {

    @Autowired
    private KafkaTemplate<String , String> kafkaTemplate;

    private static Logger logger = LoggerFactory.getLogger(LoggerController.class);

    @PostMapping("/log")
    public String doLog(@RequestParam("log") String logJson){
        //添加时间戳
        JSONObject object = JSON.parseObject(logJson);
        object.put("ts",System.currentTimeMillis());

        //使用log4j对日志进行落盘
        logger.info(object.toJSONString());

        //推送数据到Kafka
        if ("startup".equals(object.get("type"))){
            kafkaTemplate.send(Constant.KAFKA_TOPIC_STARTUP,object.toJSONString());
        }else {
            kafkaTemplate.send(Constant.KAFKA_TOPIC_EVENT,object.toJSONString());
        }

        System.out.println(logJson);
        return "success";
    }
}
