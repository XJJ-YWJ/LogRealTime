package Util;

import com.study.common.Constant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * kakfa的工具类
 */
public class KafkaUtil {

    public static JavaInputDStream<ConsumerRecord<String, String>> getKafkaStream(JavaStreamingContext jssc){
        Collection<String> TOPICS = new HashSet<>();
        TOPICS.add(Constant.KAFKA_TOPIC_STARTUP);
        TOPICS.add(Constant.KAFKA_TOPIC_EVENT);
        TOPICS.add(Constant.KAFKA_TOPIC_ORDER);

        Map<String, Object> kafkaPrams = new HashMap<>();
        //Kafka服务监听端口
        kafkaPrams.put("bootstrap.servers","192.168.30.67:9092");
        //指定kafka输出key的数据类型及编码格式（默认为字符串类型编码格式为uft-8）
        kafkaPrams.put("key.deserializer", StringDeserializer.class);
        //指定kafka输出value的数据类型及编码格式（默认为字符串类型编码格式为uft-8）
        kafkaPrams.put("value.deserializer",StringDeserializer.class);
        //消费者ID，随意指定
        kafkaPrams.put("group.id","xjj");
        //指定从latest(最新,其他版本的是largest这里不行)还是smallest(最早)处开始读取数据
        kafkaPrams.put("auto.offset.reset","latest");
        //如果true,consumer定期地往zookeeper写入每个分区的offset
        kafkaPrams.put("enable.auto.commit",false);

        JavaInputDStream<ConsumerRecord<String, String>> directStream = KafkaUtils.createDirectStream(jssc,
                LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(TOPICS, kafkaPrams));

        return directStream;
    }

}
