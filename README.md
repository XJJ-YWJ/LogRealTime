# LogRealTime
基于spark的大数据日志实时分析项目
#### 项目内容
1. realTime-mock:模拟日志数据
2. realTime-logger:接收模拟数据到日志服务器中,将数据落盘到服务器并推送到Kafka
3. realTime-realtime:通过Spark Streaming 接收Kakfa中的数据,并通过redis进行去重,去重后的数据保存到Elasticsearch 中方便后面的数据检索
4. realTime-publish:基于SpringBoot封装通过Elasticsearch 检索过后的数据,并封装数据发布接口
5. realTime-canal:通过阿里云的开源组件,利用MySQL的BinLog主从复制,通过canal抓取数据,封装数据到对象中,保存到ES中方便数据检索
6. dw-chart:前端模块,通过Ajax请求数据接口,对最终处理的数据进行展示

#### 开发环境

1. IDEA 2020.1   JDK8
2. Spring Boot 2.3.3
3. Elasticsearch 7.7.0
4. Kinaba 7.7.0
5. Redis 6.X
6. Spark 2.4.4
7. Canal 1.1.5
8. Spring-data-elasticsearch 4.0
9. Hive 2.3.6

#### 项目效果 
![image](https://github.com/XJJ-YWJ/LogRealTime/blob/master/XU.png)
