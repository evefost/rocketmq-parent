package com.xie.test.a.vjia;

import com.xie.test.a.ApplicationA;

import java.util.Arrays;
import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

/**
 * @author jiaguofang
 * @version 1.0.0
 * @classname VjiaSpringTest
 * @description TODO
 * @created 3/13/2019 09:46
 */
@SpringBootTest(classes = ApplicationA.class, properties = {"spring.profiles.active=test"})
public class VjiaSpringTest
//    extends AbstractTestNGSpringContextTests
{

    @Resource
    private Jedis jedis;

    @Test
    public void testExecuteLuaScript() {
        String skuId = "1234567";
        String phone = "1234567";
        String orderId = "123456";
        String countKey = null;// RedisHelper.key(skuId, phone, "count");
        String orderKey = null;// RedisHelper.key(skuId, phone, "order");
//        Object result = jedis.eval(LuaScript.DECREASE_COUNT, Arrays.asList(countKey, orderKey), Arrays.asList(orderId, "100"));
        Object result = jedis.eval("", Arrays.asList(countKey, orderKey), Arrays.asList(orderId, "100"));
        System.out.println("evel result is " + result.toString());
    }

}
