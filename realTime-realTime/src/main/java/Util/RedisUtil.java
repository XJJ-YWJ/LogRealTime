package Util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis工具类
 */
public class RedisUtil {
    private static JedisPool jedisPool = null;

    public static Jedis getJedisClient(){
        if (jedisPool == null){
            JedisPoolConfig config = new JedisPoolConfig();
            //设置最大连接数
            config.setMaxTotal(100);
            //最大空闲
            config.setMaxIdle(20);
            //最小空闲
            config.setMinIdle(20);
            //忙碌时是否等待
            config.setBlockWhenExhausted(true);
            //忙碌时等待时长  毫秒
            config.setMaxWaitMillis(500);
            //每次获得连接进行测试
            config.setTestOnBorrow(true);

            jedisPool = new JedisPool(config, "192.168.30.67", 6379);
        }

        return jedisPool.getResource();
    }

    public static void main(String[] args) {
        System.out.println(getJedisClient());
    }
}
