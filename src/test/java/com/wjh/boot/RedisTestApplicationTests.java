package com.wjh.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RedisTestApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        Jedis jedis = new Jedis("81.68.254.220",6379);
//        System.out.println(jedis.ping());
//        System.out.println(jedis.zrange("zset01",0,-1));
//        System.out.println(jedis.zrangeWithScores("zset01",0,-1));
        Map<String,String> map = new HashMap<String,String>();
        map.put("name", "zhangsan");
        map.put("age","21");
        map.put("hobby","listenMusic");
        jedis.hmset("wjhInfo",map);

    }

    @Test
    @DisplayName("Redis事务测试")
    void testTX(){
        int money = 200;

        Jedis jedis = new Jedis("81.68.254.220",6379);
//        jedis.set("accountA","1000");
//        jedis.set("accountB","300");

        if(Integer.parseInt(jedis.get("accountA")) > 0){
            jedis.watch("accountA");
            jedis.watch("accountB");

            Transaction transaction = jedis.multi();
            transaction.decrBy("accountA",money);
            transaction.incrBy("accountB",money);
            transaction.exec();
            System.out.println("accountA余额为："+jedis.get("accountA"));
            System.out.println("accountB余额为："+jedis.get("accountB"));

        }else {
            System.out.println("accountA余额不足");
        }

    }

}
