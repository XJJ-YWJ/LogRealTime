package App;

import Bean.StartUpLog;
import Util.EsUtil;
import Util.KafkaUtil;
import Util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.study.common.Constant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import redis.clients.jedis.Jedis;
import scala.Tuple2;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计日活跃用户
 */

public class APPDAU {

    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("DAU").setMaster("local[*]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));

        JavaInputDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtil.getKafkaStream(jssc);

        JavaDStream<String> dStream = kafkaStream.flatMap(recode -> Arrays.asList(recode.value()).iterator());
        //dStream.print();

        //转换消费到的数据   返回StartUpLog对象的流
        JavaDStream<StartUpLog> startUpLogStream = kafkaStream.map(recode -> {
            String JsonStr = recode.value();
            StartUpLog startUpLog = JSON.parseObject(JsonStr, StartUpLog.class);
            Long ts = startUpLog.getTs();
            Date date = new Date(ts);
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
            startUpLog.setLogDate(format.split(" ")[0]);
            startUpLog.setLogHour(format.split(" ")[1].split(":")[0]);
            startUpLog.setLogHourMinute(format.split(" ")[1].split(":")[1]);
            return startUpLog;
        });


        //使用redis进行去重
        JavaDStream<StartUpLog> filterDStream = startUpLogStream.transform((Function<JavaRDD<StartUpLog>, JavaRDD<StartUpLog>>) v1 -> {
            System.out.println("数据过滤前:" + v1.count());
            String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Jedis jedis = RedisUtil.getJedisClient();
            //当前redis中已经存在的数据
            Set<String> dauSet = jedis.smembers("dau:" + curDate);
            //存放数据到广播变量中
            Broadcast<Set<String>> dauBC = jssc.sparkContext().broadcast(dauSet);
            JavaRDD<StartUpLog> filter = v1.filter(startUpLog -> {
                Set<String> set = dauBC.value();
                return !set.contains(startUpLog.getMid());
            });
            System.out.println("数据过滤后:"+filter.count());
            System.out.println("=======================================");
            return filter;
        });


        //进一步处理相同数据,处理同一批次中相同mid的数据
        JavaPairDStream<String, Iterable<StartUpLog>> groupByKey = filterDStream.mapToPair(startUpLog -> new Tuple2<>(startUpLog.getMid(),startUpLog)).groupByKey();
        JavaDStream<StartUpLog> distantDStream = groupByKey.map(v1 -> v1._2.iterator().next());

        //distantDStream.print();

        //保存数据到redis
        distantDStream.foreachRDD(rdd -> {   //Drive端执行
            /**
             * Redis 使用Set类型的数据结构保存数据
             *      key : dau+日期
             *      value : mid
             */
            rdd.foreachPartition(startUpLogItr -> {    //Executor端执行
                List<StartUpLog> list = new ArrayList<>();
                for (Iterator<StartUpLog> it = startUpLogItr; it.hasNext();){
                    StartUpLog next = it.next();
                    list.add(next);
                }
                //获取jedis客户端连接
                Jedis jedis = RedisUtil.getJedisClient();
                for (StartUpLog startUpLog : list) {
                    String key = "dau:"+startUpLog.getLogDate();
                    String value = startUpLog.getMid();
                    jedis.sadd(key,value);
                }
                //插入数据到ES
                EsUtil.indexBulkStartUpLog(Constant.ES_INDEX_DAU,list);

                //关闭连接
                jedis.close();
            });

        });


        jssc.start();
        jssc.awaitTermination();
    }
}
