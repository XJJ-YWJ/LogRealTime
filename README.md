# LogRealTime
基于spark的大数据日志实时分析项目
#### 项目内容

基于App客户端的日志,模拟数据推送到日志服务器,日志服务器通过redis和spark去重后推送到Kafka中,实时计算的客户端开始消费kafka中的日志信息,推送到Elasticsearch ,并经过Elasticsearch 查询处理后返回结果



#### 开发环境

1. IDEA 2020.1
2. Spring Boot 2.3.3
3. Elasticsearch 7.7.0
4. Kinaba 7.7.0
5. Redis 6.X
6. Spark 2.4.4

#### 项目效果



