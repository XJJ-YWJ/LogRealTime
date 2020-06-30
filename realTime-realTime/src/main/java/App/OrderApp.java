package App;

import Bean.OrderLog;
import Util.EsUtil;
import Util.KafkaUtil;
import com.alibaba.fastjson.JSON;
import com.study.common.Constant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 订单数据推送到ES
 */
public class OrderApp {

    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("ORDER").setMaster("local[*]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));

        JavaInputDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtil.getKafkaStream(jssc);

        JavaDStream<OrderLog> orderLogJavaDStream = kafkaStream.map(record -> {
            String jsonStr = record.value();
            OrderLog orderLog = JSON.parseObject(jsonStr, OrderLog.class);
            //数据脱敏
            String tel = orderLog.getConsigneeTel();
            //提取手机号前四位
            String substring = tel.substring(0, 4);
            orderLog.setConsigneeTel(substring + "********");
            orderLog.setCreateDate(orderLog.getCreateTime().split(" ")[0]);
            orderLog.setCreateHour(Integer.parseInt(orderLog.getCreateTime().split(" ")[1].split(":")[0]));
            orderLog.setCreateHourMinute(orderLog.getCreateTime().split(" ")[1].split(":")[1]+":"+
                    orderLog.getCreateTime().split(" ")[1].split(":")[2]);

            return orderLog;
        });

        //推送数据到ES中
        orderLogJavaDStream.foreachRDD(rdd ->
                rdd.foreachPartition(orderLogIte -> {

                    List<OrderLog> orderLogs = new ArrayList<>();
                    for(Iterator<OrderLog> orderLog = orderLogIte; orderLog.hasNext();){
                        OrderLog next = orderLog.next();
                        orderLogs.add(next);
                    }

                    EsUtil.indexBulkOrderLog(Constant.ES_INDEX_ORDER,orderLogs);
                })
        );

        jssc.start();
        jssc.awaitTermination();
    }
}
